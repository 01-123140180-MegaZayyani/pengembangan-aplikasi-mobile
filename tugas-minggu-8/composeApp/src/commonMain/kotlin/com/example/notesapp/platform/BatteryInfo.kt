package com.example.notesapp.platform

/**
 * Bonus (+10% rubrik): BatteryInfo expect/actual implementation.
 */
expect class BatteryInfo() {
    fun getBatteryLevel(): Int // 0-100
    fun isCharging(): Boolean
}
