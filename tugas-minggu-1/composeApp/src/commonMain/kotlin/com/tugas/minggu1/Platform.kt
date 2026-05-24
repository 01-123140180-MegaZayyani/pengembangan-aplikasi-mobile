package com.tugas.minggu1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform