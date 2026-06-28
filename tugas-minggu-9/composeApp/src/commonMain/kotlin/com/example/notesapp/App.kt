package com.example.notesapp

import androidx.compose.runtime.Composable
import com.example.notesapp.presentation.AppNavigation
import com.example.notesapp.presentation.viewmodel.NotesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    // Dependency (repository, settings, dst) sudah di-inject otomatis oleh Koin
    val viewModel: NotesViewModel = koinViewModel()
    AppNavigation(viewModel = viewModel)
}
