package com.example.notesapp.data.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request & Response DTOs untuk Google Gemini API.
 * Lihat slide 14 (Materi 09 - Gemini Data Models).
 */
@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig? = null,
    val safetySettings: List<GeminiSafetySetting>? = null
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String = "user"
)

@Serializable
data class GeminiPart(
    val text: String
)

@Serializable
data class GeminiGenerationConfig(
    val temperature: Double = 0.4,
    @SerialName("maxOutputTokens") val maxOutputTokens: Int = 512,
    val topP: Double = 0.95
)

/**
 * Catatan pribadi/curhatan sering ke-flag oleh safety filter default Gemini
 * (kategori HARASSMENT/HATE_SPEECH/dll bisa salah deteksi pada cerita emosional).
 * Kita longgarkan threshold-nya agar isi curhatan tetap bisa diringkas,
 * KECUALI kategori yang berkaitan dengan self-harm tetap dibiarkan default
 * (tidak dilonggarkan) demi keamanan pengguna.
 */
@Serializable
data class GeminiSafetySetting(
    val category: String,
    val threshold: String
)

val defaultSafetySettings = listOf(
    GeminiSafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_ONLY_HIGH"),
    GeminiSafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_ONLY_HIGH"),
    GeminiSafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_ONLY_HIGH"),
    GeminiSafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_ONLY_HIGH")
)

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate> = emptyList(),
    val promptFeedback: GeminiPromptFeedback? = null
)

@Serializable
data class GeminiCandidate(
    val content: GeminiContent? = null,
    val finishReason: String? = null
)

@Serializable
data class GeminiPromptFeedback(
    val blockReason: String? = null
)