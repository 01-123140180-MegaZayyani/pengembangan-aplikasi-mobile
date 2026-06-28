package com.example.notesapp.data.repository

import com.example.notesapp.data.ai.GeminiService

/**
 * Repository pattern untuk fitur AI (lihat slide 24 Materi 09).
 * Saat ini hanya menyediakan fitur "Auto-Summarize" untuk catatan.
 */
interface AIRepository {
    suspend fun summarizeNote(content: String): Result<String>
}

class AIRepositoryImpl(
    private val geminiService: GeminiService
) : AIRepository {

    override suspend fun summarizeNote(content: String): Result<String> {
        if (content.isBlank()) {
            return Result.failure(IllegalArgumentException("Catatan masih kosong, tidak ada yang bisa diringkas"))
        }

        // System prompt: role + task + format + constraint (lihat slide 19-20 Materi 09)
        val prompt = """
            Kamu adalah asisten penulisan yang ahli meringkas catatan pribadi.

            Tugas: Ringkas isi catatan di bawah ini menjadi maksimal 3 kalimat,
            ambil hanya poin-poin paling penting.

            Rules:
            - Jawab dalam Bahasa Indonesia
            - Jangan menambahkan informasi yang tidak ada di catatan
            - Jangan gunakan format markdown, tulis sebagai paragraf biasa
            - Jika catatan sudah sangat singkat, cukup tulis ulang dengan lebih rapi

            Catatan:
            ${content.trim()}
        """.trimIndent()

        return geminiService.generateContent(prompt)
    }
}
