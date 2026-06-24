package com.example.notesapp.navigation

/**
 * Sealed class untuk item Bottom Navigation.
 * Menggunakan emoji sebagai icon karena tidak ada dependency Icons.
 */
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String  // emoji icon
) {
    object Notes : BottomNavItem(
        route = Screen.Notes.route,
        label = "Notes",
        icon = "📝"
    )

    object Favorites : BottomNavItem(
        route = Screen.Favorites.route,
        label = "Favorites",
        icon = "❤️"
    )

    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        label = "Profile",
        icon = "👤"
    )

    companion object {
        val items = listOf(Notes, Favorites, Profile)
    }
}
