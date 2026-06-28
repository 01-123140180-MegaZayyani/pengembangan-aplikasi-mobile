package com.example.notesapp.platform

/**
 * expect class - deklarasi API device info di common code.
 * Implementasi sebenarnya (actual) ada di masing-masing platform:
 * - androidMain -> DeviceInfo.android.kt
 * - iosMain     -> DeviceInfo.ios.kt
 * - jvmMain     -> DeviceInfo.jvm.kt
 */
expect class DeviceInfo() {
    fun getDeviceName(): String
    fun getOsVersion(): String
    fun getAppVersion(): String
}
