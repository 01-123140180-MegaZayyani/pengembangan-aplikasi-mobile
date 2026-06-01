package org.example.projectnewsfeed.model

data class NewsItem(
    val id: Int,
    val title: String,
    val category: String,
    val source: String
)

// Sample data untuk simulasi
val sampleNews = listOf(
    NewsItem(1, "Kotlin 2.0 Resmi Dirilis", "teknologi", "TechNews"),
    NewsItem(2, "Piala Dunia 2026 Kick-off", "olahraga", "SportHub"),
    NewsItem(3, "AI Masuk Kurikulum Nasional", "teknologi", "EduTech"),
    NewsItem(4, "Startup Lokal Raih 10M USD", "bisnis", "BizDaily"),
    NewsItem(5, "KMP Kini Support iOS 18", "teknologi", "DevWorld"),
    NewsItem(6, "Liga 1 Musim Baru Dimulai", "olahraga", "GoalID"),
)
