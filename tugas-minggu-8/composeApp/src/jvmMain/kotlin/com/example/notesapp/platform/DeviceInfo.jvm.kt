package com.example.notesapp.platform

actual class DeviceInfo actual constructor() {
    actual fun getDeviceName(): String =
        System.getProperty("os.name") ?: "Desktop"
    actual fun getOsVersion(): String =
        "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
    actual fun getAppVersion(): String = "1.0.0"
}
