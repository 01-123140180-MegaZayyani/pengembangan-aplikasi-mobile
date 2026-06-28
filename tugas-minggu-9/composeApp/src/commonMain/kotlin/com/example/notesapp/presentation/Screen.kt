package com.example.notesapp.presentation

sealed class Screen(val route: String) {
    object Notes     : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")

    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }

    object AddNote : Screen("add_note")

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Long) = "edit_note/$noteId"
    }

    object Settings : Screen("settings")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Notes,     "Catatan"),
    BottomNavItem(Screen.Favorites, "Favorit"),
    BottomNavItem(Screen.Profile,   "Profil")
)
