package com.example.notesapp.domain.model

/**
 * Domain model untuk Note.
 * Ini adalah representasi data yang digunakan di seluruh app (bukan entity database langsung).
 */
data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
