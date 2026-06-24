package com.example.notesapp.model

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val color: NoteColor = NoteColor.DEFAULT,
    val createdAt: String = ""
)

enum class NoteColor(val hex: String) {
    DEFAULT("#2C2C3E"),
    YELLOW("#3D3A1F"),
    GREEN("#1F3D2A"),
    BLUE("#1F2A3D"),
    PINK("#3D1F2E")
}
