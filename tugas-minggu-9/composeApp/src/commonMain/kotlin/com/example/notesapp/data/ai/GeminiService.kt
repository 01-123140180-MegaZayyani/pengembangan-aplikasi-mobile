package com.example.notesapp.data.ai

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

/**
 * Error khusus untuk fitur AI, mengikuti pola "Common AI API Errors"
 * pada slide 30-31 Materi 09.
 *
 * Pesan di sini SENGAJA dibuat singkat & ramah karena ditampilkan langsung
 * ke UI. Detail teknis (raw response, status code, dll) hanya di-log lewat
 * println() / Logcat, tidak ditampilkan ke user.
 */
sealed class AIError(message: String) : Exception(message) {
    data class Unauthorized(override val message: String) : AIError(message)
    data class RateLimited(override val message: String) : AIError(message)
    data class ServerError(override val message: String) : AIError(message)
    data class NetworkError(override val message: String) : AIError(message)
    data class ParseError(override val message: String) : AIError(message)
    data class Unknown(override val message: String) : AIError(message)
}

/**
 * Service untuk memanggil Google Gemini API (generateContent).
 * Lihat slide 15 Materi 09 - Gemini Service.
 *
 * PENTING: "gemini-2.0-flash" sudah di-shutdown oleh Google (per 1 Juni 2026).
 * Kalau ke depannya model di bawah ini juga dideprecate, cek daftar model aktif di
 * https://ai.google.dev/gemini-api/docs/models lalu ganti nilai `model`.
 */
class GeminiService(private val client: HttpClient) {

    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val model = "gemini-2.5-flash"

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    suspend fun generateContent(prompt: String): Result<String> {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(parts = listOf(GeminiPart(text = prompt)))
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.4,
                    maxOutputTokens = 512
                ),
                safetySettings = defaultSafetySettings
            )

            val httpResponse: HttpResponse = client.post(
                "$baseUrl/models/$model:generateContent"
            ) {
                contentType(ContentType.Application.Json)
                parameter("key", AIConfig.GEMINI_API_KEY)
                setBody(request)
            }

            val rawBody = httpResponse.bodyAsText()
            // Detail teknis HANYA masuk console/Logcat, tidak pernah ditampilkan ke UI.
            println("[GeminiService] HTTP ${httpResponse.status.value} - raw response: $rawBody")

            // Ktor tidak selalu melempar exception untuk status non-2xx (tergantung
            // konfigurasi engine), jadi status code dicek manual di sini supaya
            // error seperti 503/500/429 selalu tertangani dengan benar.
            if (!httpResponse.status.isSuccess()) {
                return Result.failure(mapStatusToError(httpResponse.status, rawBody))
            }

            val response: GeminiResponse = json.decodeFromString(rawBody)
            val candidate = response.candidates.firstOrNull()
            val text = candidate?.content?.parts?.firstOrNull()?.text

            if (!text.isNullOrBlank()) {
                Result.success(text.trim())
            } else {
                println(
                    "[GeminiService] Empty result. blockReason=${response.promptFeedback?.blockReason}, " +
                            "finishReason=${candidate?.finishReason}"
                )
                when {
                    response.promptFeedback?.blockReason != null || candidate?.finishReason == "SAFETY" ->
                        Result.failure(AIError.ParseError("AI menolak meringkas catatan ini karena terdeteksi sebagai konten sensitif."))

                    candidate?.finishReason == "MAX_TOKENS" ->
                        Result.failure(AIError.ParseError("Catatan terlalu panjang untuk diringkas sekali jalan."))

                    else ->
                        Result.failure(AIError.ParseError("AI tidak mengembalikan ringkasan. Coba lagi."))
                }
            }
        } catch (e: ClientRequestException) {
            val errorBody = try { e.response.bodyAsText() } catch (_: Exception) { "" }
            println("[GeminiService] ClientRequestException ${e.response.status.value}: $errorBody")
            Result.failure(mapStatusToError(e.response.status, errorBody))
        } catch (e: ServerResponseException) {
            val errorBody = try { e.response.bodyAsText() } catch (_: Exception) { "" }
            println("[GeminiService] ServerResponseException ${e.response.status.value}: $errorBody")
            Result.failure(mapStatusToError(e.response.status, errorBody))
        } catch (e: SerializationException) {
            println("[GeminiService] SerializationException: ${e.message}")
            Result.failure(AIError.ParseError("Gagal membaca respons dari AI. Coba lagi."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            println("[GeminiService] Exception: ${e.message}")
            Result.failure(AIError.NetworkError("Tidak ada koneksi internet. Periksa jaringan Anda"))
        }
    }

    /** Memetakan HTTP status code dari Gemini menjadi pesan singkat & ramah untuk UI. */
    private fun mapStatusToError(status: HttpStatusCode, rawBody: String): AIError {
        println("[GeminiService] Error body for status ${status.value}: $rawBody")
        return when (status.value) {
            401, 403 -> AIError.Unauthorized("API key tidak valid. Periksa AIConfig.GEMINI_API_KEY")
            404 -> AIError.Unknown("Model AI tidak ditemukan atau sudah deprecated")
            429 -> AIError.RateLimited("Terlalu banyak request. Coba lagi sebentar")
            503 -> AIError.ServerError("Server AI sedang sibuk (banyak permintaan). Coba lagi sebentar")
            in 500..599 -> AIError.ServerError("Server AI sedang bermasalah, coba lagi nanti")
            else -> AIError.Unknown("Gagal memproses permintaan ke AI")
        }
    }
}