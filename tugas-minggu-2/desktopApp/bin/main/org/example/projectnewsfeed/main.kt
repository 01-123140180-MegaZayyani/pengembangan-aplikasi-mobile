package org.example.projectnewsfeed

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

fun main() = runBlocking {
    val manager = NewsFeedManager()

    // Pantau jumlah berita yang sudah dibaca (StateFlow)
    launch {
        manager.readCount.collect { count ->
            if (count > 0) println("\n📊 Total berita dibaca: $count")
        }
    }

    println("🚀 News Feed dimulai — filter: teknologi\n")

    // Mulai feed dengan filter kategori "teknologi"
    manager.startFeed(this, "teknologi")
}