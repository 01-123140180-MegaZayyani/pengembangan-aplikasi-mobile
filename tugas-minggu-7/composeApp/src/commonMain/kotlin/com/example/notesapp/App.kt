package com.example.notesapp

import androidx.compose.runtime.Composable
import com.example.notesapp.presentation.AppNavigation
import com.example.notesapp.presentation.viewmodel.NotesViewModel

@Composable
fun App(viewModel: NotesViewModel) {
    AppNavigation(viewModel = viewModel)
}
