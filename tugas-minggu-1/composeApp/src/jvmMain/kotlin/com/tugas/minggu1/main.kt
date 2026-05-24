package com.tugas.minggu1

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TugasMinggu1",
    ) {
        App()
    }
}