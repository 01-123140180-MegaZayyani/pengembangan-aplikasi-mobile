package org.notesapp.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.notesapp.data.db.DatabaseDriverFactory
import com.example.notesapp.data.repository.NoteRepository
import com.example.notesapp.data.settings.SettingsManager
import com.example.notesapp.presentation.AppNavigation
import com.example.notesapp.presentation.viewmodel.NotesViewModel
import com.notes.app.db.NotesDatabase
import com.russhwolf.settings.Settings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val driverFactory = DatabaseDriverFactory(this)
        val database = NotesDatabase(driverFactory.createDriver())
        val repository = NoteRepository(database)
        val settings = com.russhwolf.settings.SharedPreferencesSettings(getSharedPreferences("notes_settings", MODE_PRIVATE))
        val settingsManager = SettingsManager(settings)
        val viewModel = NotesViewModel(repository, settingsManager)

        setContent {
            MaterialTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
