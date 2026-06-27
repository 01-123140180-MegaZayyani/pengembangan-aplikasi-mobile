package org.notesapp.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.notesapp.App
import com.example.notesapp.di.appModules
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(appModules)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "NotesAppProject",
        ) {
            App()
        }
    }
}
