package com.example.notesapp.data.ai
import com.example.notesapp.BuildKonfig

/**
 * Konfigurasi untuk AI API (Pertemuan 9 - Integrasi AI API).
 *
 * API key sekarang disimpan dengan aman di local.properties dan
 * diakses melalui BuildKonfig untuk mendukung semua platform.
 */
object AIConfig {
    val GEMINI_API_KEY: String = BuildKonfig.GEMINI_API_KEY
}
