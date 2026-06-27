package com.example.notesapp.platform

actual class BatteryInfo actual constructor() {
    // Desktop/JVM tidak punya API baterai standar - dikembalikan nilai default.
    actual fun getBatteryLevel(): Int = 100
    actual fun isCharging(): Boolean = true
}
