package com.example.notesapp.platform

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Implementasi sederhana untuk iOS. Pengecekan koneksi real-time yang lebih
 * akurat bisa memakai NWPathMonitor (Network framework), namun untuk
 * kebutuhan latihan ini cukup mengembalikan status "online" secara default
 * sehingga UI (NetworkStatusIndicator) tetap berfungsi tanpa crash di iOS.
 */
actual class NetworkMonitor {
    actual fun isConnected(): Boolean = true
    actual fun observeConnectivity(): Flow<Boolean> = flowOf(true)
}
