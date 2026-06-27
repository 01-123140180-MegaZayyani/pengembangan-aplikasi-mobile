package org.app.newsreader.project.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory untuk membuat HttpClient Ktor yang dipakai di seluruh aplikasi.
 * Sesuai materi pertemuan 6: ContentNegotiation + Kotlinx Serialization + Logging + Timeout.
 */
object HttpClientFactory {
    fun create(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true // anti JSON parse error kalau ada field baru
                    }
                )
            }
            install(Logging) {
                level = LogLevel.INFO
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 15_000
            }
        }
    }
}
