package org.app.newsreader.project.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Model artikel berita.
 * Memetakan response dari JSONPlaceholder (/posts) ke bentuk "artikel".
 * field "body" dari API kita anggap sebagai description/content artikel.
 * field "image" dibuat sendiri (picsum.photos) karena JSONPlaceholder tidak punya gambar.
 */
@Serializable
data class Article(
    val id: Int,
    @SerialName("userId")
    val userId: Int,
    val title: String,
    @SerialName("body")
    val description: String
) {
    // URL gambar dummy berdasarkan id, supaya tiap artikel punya gambar berbeda & konsisten
    val imageUrl: String
        get() = "https://picsum.photos/seed/$id/600/400"
}

/**
 * Body yang dikirim saat membuat artikel baru (POST), mengikuti format JSONPlaceholder.
 */
@Serializable
data class CreateArticleRequest(
    val title: String,
    val body: String,
    val userId: Int = 1
)
