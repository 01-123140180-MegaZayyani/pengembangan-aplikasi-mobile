package com.example.notesapp.navigation

/**
 * Sealed class untuk mendefinisikan semua routes dalam aplikasi.
 * Best practice: semua routes terpusat di satu tempat.
 */
sealed class Screen(val route: String) {

    // Bottom Navigation Screens
    object Notes : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")

    // Detail Screens (non-bottom nav)
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }

    object AddNote : Screen("add_note")

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
}
