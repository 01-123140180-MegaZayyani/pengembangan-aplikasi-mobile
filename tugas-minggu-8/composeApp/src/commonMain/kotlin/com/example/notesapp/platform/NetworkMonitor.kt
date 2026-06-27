package com.example.notesapp.platform

import kotlinx.coroutines.flow.Flow

/**
 * expect class - deklarasi API network monitor di common code.
 * Tidak ada constructor yang dideklarasikan di sini karena setiap platform
 * butuh dependency yang berbeda (Android butuh Context, iOS & JVM tidak).
 * Instance-nya dibuat lewat Koin (lihat di/AppModule).
 */
expect class NetworkMonitor {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
}
