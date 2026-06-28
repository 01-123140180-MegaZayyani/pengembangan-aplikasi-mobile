package com.example.notesapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.notesapp.presentation.viewmodel.NotesViewModel

@Composable
fun NotesAppTheme(
    viewModel: NotesViewModel,
    content: @Composable () -> Unit
) {
    val themeSetting by viewModel.theme.collectAsState()
    
    val darkTheme = when (themeSetting) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
