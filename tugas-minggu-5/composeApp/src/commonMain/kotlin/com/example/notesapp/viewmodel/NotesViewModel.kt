package com.example.notesapp.viewmodel

import com.example.notesapp.model.Note
import com.example.notesapp.model.NoteColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel {

    // Sample data untuk demo
    private val sampleNotes = listOf(
        Note(1, "Belajar Kotlin", "Kotlin adalah bahasa pemrograman modern yang berjalan di JVM. Kotlin multiplatform memungkinkan kita menulis kode yang bisa dijalankan di Android, iOS, dan platform lain.", true, NoteColor.YELLOW, "10 Jun 2025"),
        Note(2, "Compose Multiplatform", "Compose Multiplatform adalah framework UI deklaratif dari JetBrains yang memungkinkan pembuatan UI yang bisa berjalan di berbagai platform dengan satu codebase.", false, NoteColor.BLUE, "11 Jun 2025"),
        Note(3, "Navigasi di Compose", "Navigation Component terdiri dari NavHost, NavController, dan Routes. NavController mengatur perpindahan antar screen. NavHost adalah container yang menampung semua destinations.", true, NoteColor.GREEN, "12 Jun 2025"),
        Note(4, "MVVM Architecture", "Model-View-ViewModel adalah pattern arsitektur yang memisahkan logika bisnis dari UI. ViewModel menyimpan state dan expose data melalui StateFlow.", false, NoteColor.PINK, "13 Jun 2025"),
        Note(5, "State Management", "State dalam Compose dikelola dengan remember dan mutableStateOf. State Hoisting memindahkan state ke parent composable agar bisa digunakan bersama.", true, NoteColor.DEFAULT, "14 Jun 2025"),
        Note(6, "REST API dengan Ktor", "Ktor adalah HTTP client untuk Kotlin Multiplatform. Digunakan untuk melakukan request ke server dan parsing JSON response.", false, NoteColor.YELLOW, "15 Jun 2025"),
    )

    private val _notes = MutableStateFlow(sampleNotes)
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    val favoriteNotes: StateFlow<List<Note>>
        get() = MutableStateFlow(_notes.value.filter { it.isFavorite }).asStateFlow()

    fun getNoteById(id: Int): Note? = _notes.value.find { it.id == id }

    fun addNote(title: String, content: String) {
        val newId = (_notes.value.maxOfOrNull { it.id } ?: 0) + 1
        val newNote = Note(
            id = newId,
            title = title,
            content = content,
            isFavorite = false,
            color = NoteColor.DEFAULT,
            createdAt = "25 Jun 2025"
        )
        _notes.update { currentList -> currentList + newNote }
    }

    fun updateNote(id: Int, title: String, content: String) {
        _notes.update { currentList ->
            currentList.map { note ->
                if (note.id == id) note.copy(title = title, content = content)
                else note
            }
        }
    }

    fun toggleFavorite(id: Int) {
        _notes.update { currentList ->
            currentList.map { note ->
                if (note.id == id) note.copy(isFavorite = !note.isFavorite)
                else note
            }
        }
    }

    fun deleteNote(id: Int) {
        _notes.update { currentList -> currentList.filter { it.id != id } }
    }

    fun getFavorites(): List<Note> = _notes.value.filter { it.isFavorite }
    fun getAllNotes(): List<Note> = _notes.value
}
