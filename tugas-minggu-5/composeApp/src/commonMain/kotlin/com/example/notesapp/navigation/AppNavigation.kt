package com.example.notesapp.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesapp.screens.*
import com.example.notesapp.viewmodel.NotesViewModel

/**
 * AppNavigation - Root composable yang mengatur seluruh navigasi.
 *
 * Struktur:
 * - NavHost (container utama)
 *   - Bottom Nav Destinations: notes, favorites, profile
 *   - Detail Destinations: note_detail/{noteId}, add_note, edit_note/{noteId}
 */
@Composable
fun AppNavigation() {
    // 1. Buat satu instance ViewModel yang di-share antar screen
    val viewModel = remember { NotesViewModel() }

    // 2. Buat NavController - central API untuk navigasi
    val navController = rememberNavController()

    // 3. Scaffold dengan Bottom Navigation Bar
    Scaffold(
        bottomBar = {
            // Hanya tampilkan Bottom Nav saat berada di salah satu tab utama
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            val bottomNavRoutes = BottomNavItem.items.map { it.route }

            if (currentRoute in bottomNavRoutes) {
                NavigationBar(
                    containerColor = Color(0xFF16213E),
                    tonalElevation = 0.dp,
                    modifier = Modifier.height(72.dp)
                ) {
                    BottomNavItem.items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                // Navigasi dengan popUpTo untuk menghindari stack menumpuk
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Text(
                                    text = item.icon,
                                    fontSize = if (currentRoute == item.route) 24.sp else 20.sp
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (currentRoute == item.route) FontWeight.Bold else FontWeight.Normal,
                                    color = if (currentRoute == item.route)
                                        Color(0xFF6C63FF)
                                    else
                                        Color.White.copy(alpha = 0.4f)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF6C63FF),
                                unselectedIconColor = Color.White.copy(alpha = 0.4f),
                                indicatorColor = Color(0xFF6C63FF).copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF1A1A2E)
    ) { paddingValues ->

        // 4. NavHost - container semua destinations
        NavHost(
            navController = navController,
            startDestination = Screen.Notes.route,  // Screen pertama saat app dibuka
            modifier = Modifier.padding(paddingValues)
        ) {

            // ===== BOTTOM NAV DESTINATIONS =====

            composable(Screen.Notes.route) {
                NotesScreen(
                    viewModel = viewModel,
                    onNoteClick = { noteId ->
                        // Navigasi ke Note Detail dengan passing noteId
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    },
                    onAddClick = {
                        navController.navigate(Screen.AddNote.route)
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    viewModel = viewModel,
                    onNoteClick = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = viewModel)
            }

            // ===== DETAIL DESTINATIONS =====

            // Note Detail - menerima noteId (Int) sebagai argument
            composable(
                route = Screen.NoteDetail.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                // Ambil argument dari backStackEntry
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
                NoteDetailScreen(
                    noteId = noteId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onEdit = { id ->
                        navController.navigate(Screen.EditNote.createRoute(id))
                    },
                    onDelete = {
                        // Setelah delete, kembali ke notes list
                        navController.popBackStack(Screen.Notes.route, inclusive = false)
                    }
                )
            }

            // Add Note
            composable(Screen.AddNote.route) {
                AddNoteScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // Edit Note - menerima noteId (Int) sebagai argument
            composable(
                route = Screen.EditNote.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
                EditNoteScreen(
                    noteId = noteId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
