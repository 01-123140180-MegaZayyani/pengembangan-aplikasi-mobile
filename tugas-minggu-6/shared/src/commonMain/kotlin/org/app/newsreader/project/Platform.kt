package org.app.newsreader.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform