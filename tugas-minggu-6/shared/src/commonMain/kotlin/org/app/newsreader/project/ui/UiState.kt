package org.app.newsreader.project.ui

/**
 * UI State generik (Bagian 5 materi).
 * Loading -> sedang fetch, Success -> data berhasil didapat, Error -> gagal fetch.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

fun <T> UiState<T>.getOrNull(): T? = (this as? UiState.Success)?.data
