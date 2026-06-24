package com.example.notesapp

import androidx.compose.runtime.Composable
import com.example.notesapp.navigation.AppNavigation

/**
 * App.kt - Entry point utama aplikasi
 * Menggantikan template default dari KMP Wizard
 *
 * Cukup panggil AppNavigation() yang mengatur seluruh
 * navigasi multi-screen aplikasi.
 */
@Composable
fun App() {
    AppNavigation()
}
