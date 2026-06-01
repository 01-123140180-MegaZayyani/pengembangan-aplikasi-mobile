package org.example.projectnewsfeed

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform