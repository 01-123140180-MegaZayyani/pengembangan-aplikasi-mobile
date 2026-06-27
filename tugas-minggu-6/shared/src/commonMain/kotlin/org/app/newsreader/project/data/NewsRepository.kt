package org.app.newsreader.project.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Repository Pattern (Bagian 4 materi):
 * Memisahkan logic pengambilan data (API) dari ViewModel.
 * ViewModel tidak perlu tahu detail HTTP, cukup panggil fungsi di repository ini.
 */
class NewsRepository(private val client: HttpClient) {

    private val baseUrl = "https://jsonplaceholder.typicode.com"

    /** GET semua artikel */
    suspend fun getArticles(): Result<List<Article>> {
        return try {
            val articles: List<Article> = client.get("$baseUrl/posts").body()
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** GET satu artikel berdasarkan id, untuk detail screen */
    suspend fun getArticleById(id: Int): Result<Article> {
        return try {
            val article: Article = client.get("$baseUrl/posts/$id").body()
            Result.success(article)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** POST artikel baru (opsional, mendemonstrasikan create) */
    suspend fun createArticle(title: String, body: String): Result<Article> {
        return try {
            val article: Article = client.post("$baseUrl/posts") {
                contentType(ContentType.Application.Json)
                setBody(CreateArticleRequest(title = title, body = body))
            }.body()
            Result.success(article)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
