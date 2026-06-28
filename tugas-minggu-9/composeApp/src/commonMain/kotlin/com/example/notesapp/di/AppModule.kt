package com.example.notesapp.di

import com.example.notesapp.data.ai.GeminiService
import com.example.notesapp.data.repository.AIRepository
import com.example.notesapp.data.repository.AIRepositoryImpl
import com.example.notesapp.data.repository.NoteRepository
import com.example.notesapp.data.settings.SettingsManager
import com.example.notesapp.presentation.viewmodel.NotesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Module yang dependency-nya sama di semua platform.
 * NoteRepository, SettingsManager, dan NotesViewModel dibangun di sini
 * dengan memanfaatkan dependency yang disuplai oleh platformModule (get()).
 *
 * Pertemuan 9: ditambahkan HttpClient + GeminiService + AIRepository
 * untuk fitur "Auto-Summarize" catatan dengan Gemini API.
 */
val commonModule = module {
    single { NoteRepository(get()) }
    single { SettingsManager(get()) }
    viewModelOf(::NotesViewModel)

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    single { GeminiService(get()) }
    single<AIRepository> { AIRepositoryImpl(get()) }
}

/**
 * expect val - setiap platform menyediakan implementasinya sendiri
 * (DatabaseDriverFactory, NotesDatabase, Settings, DeviceInfo, BatteryInfo,
 * NetworkMonitor) karena dependency-nya berbeda per platform.
 * Lihat: AppModule.android.kt, AppModule.ios.kt, AppModule.jvm.kt
 */
expect val platformModule: Module

/** Kumpulan module yang didaftarkan ke Koin saat startup. */
val appModules = listOf(commonModule, platformModule)
