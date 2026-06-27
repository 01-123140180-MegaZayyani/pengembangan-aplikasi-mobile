package com.example.notesapp.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.notesapp.presentation.screens.*
import com.example.notesapp.presentation.theme.NotesAppTheme
import com.example.notesapp.presentation.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: NotesViewModel) {
    NotesAppTheme(viewModel = viewModel) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val bottomNavRoutes = setOf(Screen.Notes.route, Screen.Favorites.route, Screen.Profile.route)
        val showBottomBar = currentDestination?.route in bottomNavRoutes

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any {
                                    it.route == item.screen.route
                                } == true,
                                onClick = {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    when (item.screen) {
                                        Screen.Notes -> Icon(
                                            Icons.Default.Edit,
                                            contentDescription = item.label
                                        )
                                        Screen.Favorites -> Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = item.label
                                        )
                                        Screen.Profile -> Icon(
                                            Icons.Default.Person,
                                            contentDescription = item.label
                                        )
                                        else -> {}
                                    }
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Notes.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Notes.route) {
                    NotesScreen(
                        viewModel = viewModel,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onAddNote = {
                            navController.navigate(Screen.AddNote.route)
                        },
                        onSettingsClick = {
                            navController.navigate(Screen.Settings.route)
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
                    ProfileScreen(
                        viewModel = viewModel,
                        onSettingsClick = {
                            navController.navigate(Screen.Settings.route)
                        }
                    )
                }

                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.LongType }
                    )
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                    NoteDetailScreen(
                        noteId = noteId,
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onEditNote = { id ->
                            navController.navigate(Screen.EditNote.createRoute(id))
                        }
                    )
                }

                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.LongType }
                    )
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                    EditNoteScreen(
                        noteId = noteId,
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
