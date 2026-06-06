package org.example.projectnewsfeed

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val manager = remember { NewsFeedManager() }

    // Pantau jumlah berita yang sudah dibaca (StateFlow)
    LaunchedEffect(Unit) {
        manager.readCount.collect { count ->
            if (count > 0) println("\n📊 Total berita dibaca: $count")
        }
    }

    // Mulai feed dengan filter kategori "teknologi"
    LaunchedEffect(Unit) {
        println("🚀 News Feed dimulai — filter: teknologi\n")
        manager.startFeed(this, "teknologi")
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "News Feed Simulator"
    ) {
        App()
    }
}
