package org.app.newsreader.project.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.app.newsreader.project.data.Article
import org.app.newsreader.project.data.NewsRepository

/**
 * ViewModel untuk list artikel.
 * Menangani loading awal dan pull-to-refresh lewat fungsi yang sama (loadArticles).
 */
class NewsListViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Article>>> = _uiState.asStateFlow()

    // Status khusus untuk indikator "pull to refresh" agar tidak menampilkan
    // full-screen loading lagi saat user menarik layar untuk refresh.
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadArticles(showFullLoading = true)
    }

    fun loadArticles(showFullLoading: Boolean = false) {
        viewModelScope.launch {
            if (showFullLoading) {
                _uiState.value = UiState.Loading
            } else {
                _isRefreshing.value = true
            }

            repository.getArticles()
                .onSuccess { articles ->
                    _uiState.value = UiState.Success(articles)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Terjadi kesalahan tidak diketahui")
                }

            _isRefreshing.value = false
        }
    }

    /** Dipanggil oleh komponen Pull-to-Refresh */
    fun refresh() = loadArticles(showFullLoading = false)

    /** Dipanggil saat tombol Retry pada error state ditekan */
    fun retry() = loadArticles(showFullLoading = true)
}

/**
 * ViewModel untuk detail screen, mengambil 1 artikel berdasarkan id.
 */
class NewsDetailViewModel(
    private val repository: NewsRepository,
    private val articleId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Article>>(UiState.Loading)
    val uiState: StateFlow<UiState<Article>> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getArticleById(articleId)
                .onSuccess { article -> _uiState.value = UiState.Success(article) }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Gagal memuat detail artikel")
                }
        }
    }
}
