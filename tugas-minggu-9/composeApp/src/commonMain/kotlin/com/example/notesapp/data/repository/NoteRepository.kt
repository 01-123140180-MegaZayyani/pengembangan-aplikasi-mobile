package com.example.notesapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.notes.app.db.NotesDatabase
import com.notes.app.db.NoteEntity
import com.example.notesapp.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class NoteRepository(database: NotesDatabase) {

    private val queries = database.noteQueries

    fun getAllNotes(): Flow<List<Note>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }
    }

    fun getFavoriteNotes(): Flow<List<Note>> {
        return queries.selectFavorites()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }
    }

    fun getNoteById(id: Long): Flow<Note?> {
        return queries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return queries.search(query, query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }
    }

    suspend fun insertNote(title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.Default) {
            queries.insert(
                title = title,
                content = content,
                created_at = now,
                updated_at = now
            )
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.Default) {
            queries.update(
                title = title,
                content = content,
                updated_at = now,
                id = id
            )
        }
    }

    suspend fun toggleFavorite(id: Long) {
        withContext(Dispatchers.Default) {
            queries.toggleFavorite(id)
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.Default) {
            queries.deleteNote(id)
        }
    }

    private fun NoteEntity.toDomain(): Note = Note(
        id = id,
        title = title,
        content = content,
        isFavorite = is_favorite == 1L,
        createdAt = created_at,
        updatedAt = updated_at
    )
}
