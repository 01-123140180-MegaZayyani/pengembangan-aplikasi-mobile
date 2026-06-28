package com.example.notesapp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.notesapp.di.appModules
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoin {
        modules(appModules)
    }
    return ComposeUIViewController { App() }
}
