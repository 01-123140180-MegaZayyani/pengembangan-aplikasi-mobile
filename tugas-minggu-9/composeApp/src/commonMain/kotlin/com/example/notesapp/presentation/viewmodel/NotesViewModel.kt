package com.example.notesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.ai.AIError
import com.example.notesapp.data.repository.AIRepository
import com.example.notesapp.data.repository.NoteRepository
import com.example.notesapp.data.settings.SettingsManager
import com.example.notesapp.domain.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager,
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _theme = MutableStateFlow(settingsManager.theme)
    val theme: StateFlow<String> = _theme.asStateFlow()

    private val _sortOrder = MutableStateFlow(settingsManager.sortOrder)
    val sortOrder: StateFlow<String> = _sortOrder.asStateFlow()

    private val _userName = MutableStateFlow(settingsManager.userName)
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _viewStyle = MutableStateFlow(settingsManager.viewStyle)
    val viewStyle: StateFlow<String> = _viewStyle.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // --- Auto-Summarize (AI Feature - Pertemuan 9) ---
    private val _isSummarizing = MutableStateFlow(false)
    val isSummarizing: StateFlow<Boolean> = _isSummarizing.asStateFlow()

    private val _summaryResult = MutableStateFlow<String?>(null)
    val summaryResult: StateFlow<String?> = _summaryResult.asStateFlow()

    private val _summaryError = MutableStateFlow<String?>(null)
    val summaryError: StateFlow<String?> = _summaryError.asStateFlow()

    val notes: StateFlow<List<Note>> = combine(
        _searchQuery.debounce(300),
        _sortOrder
    ) { query, sort ->
        query to sort
    }.flatMapLatest { (query, sort) ->
        val flow = if (query.isBlank()) repository.getAllNotes()
        else repository.searchNotes(query)
        
        flow.map { list ->
            when (sort) {
                "oldest" -> list.sortedBy { it.updatedAt }
                "alphabetical" -> list.sortedBy { it.title.lowercase() }
                else -> list.sortedByDescending { it.updatedAt }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteNotes: StateFlow<List<Note>> = _sortOrder.flatMapLatest { sort ->
        repository.getFavoriteNotes().map { list ->
            when (sort) {
                "oldest" -> list.sortedBy { it.updatedAt }
                "alphabetical" -> list.sortedBy { it.title.lowercase() }
                else -> list.sortedByDescending { it.updatedAt }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedNoteId = MutableStateFlow<Long?>(null)

    val selectedNote: StateFlow<Note?> = _selectedNoteId
        .flatMapLatest { id ->
            if (id != null) repository.getNoteById(id)
            else flowOf(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun selectNote(id: Long) {
        _selectedNoteId.value = id
    }

    fun clearSelectedNote() {
        _selectedNoteId.value = null
    }

    fun addNote(title: String, content: String) {
        if (title.isBlank() && content.isBlank()) {
            _errorMessage.value = "Judul dan konten tidak boleh kosong"
            return
        }
        viewModelScope.launch {
            try {
                repository.insertNote(
                    title = title.ifBlank { "Tanpa Judul" },
                    content = content
                )
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menyimpan catatan: ${e.message}"
            }
        }
    }

    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch {
            try {
                repository.updateNote(id, title.ifBlank { "Tanpa Judul" }, content)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memperbarui catatan: ${e.message}"
            }
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteNote(id)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menghapus catatan: ${e.message}"
            }
        }
    }

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }

    fun changeTheme(newTheme: String) {
        settingsManager.theme = newTheme
        _theme.value = newTheme
    }

    fun changeSortOrder(order: String) {
        settingsManager.sortOrder = order
        _sortOrder.value = order
    }

    fun changeUserName(name: String) {
        settingsManager.userName = name
        _userName.value = name
    }

    fun toggleViewStyle() {
        val newStyle = if (_viewStyle.value == "list") "grid" else "list"
        settingsManager.viewStyle = newStyle
        _viewStyle.value = newStyle
    }

    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Fitur AI: meringkas isi catatan menggunakan Gemini API.
     * Lihat AIRepository.summarizeNote() untuk system prompt yang digunakan.
     */
    fun summarizeNote(content: String) {
        _summaryError.value = null
        _summaryResult.value = null
        _isSummarizing.value = true

        viewModelScope.launch {
            aiRepository.summarizeNote(content)
                .onSuccess { summary ->
                    _summaryResult.value = summary
                    _isSummarizing.value = false
                }
                .onFailure { error ->
                    _summaryError.value = when (error) {
                        is AIError.Unauthorized -> error.message
                        is AIError.RateLimited -> error.message
                        is AIError.ServerError -> error.message
                        is AIError.NetworkError -> "Tidak ada koneksi internet. Periksa jaringan Anda"
                        is AIError.ParseError -> error.message
                        else -> error.message ?: "Gagal membuat ringkasan"
                    }
                    _isSummarizing.value = false
                }
        }
    }

    fun clearSummary() {
        _summaryResult.value = null
        _summaryError.value = null
        _isSummarizing.value = false
    }
}
