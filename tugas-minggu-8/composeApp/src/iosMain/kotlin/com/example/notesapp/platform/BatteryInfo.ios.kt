package com.example.notesapp.platform

import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceBatteryState

actual class BatteryInfo actual constructor() {
    init {
        UIDevice.currentDevice.batteryMonitoringEnabled = true
    }

    actual fun getBatteryLevel(): Int =
        (UIDevice.currentDevice.batteryLevel * 100).toInt()

    actual fun isCharging(): Boolean =
        UIDevice.currentDevice.batteryState == UIDeviceBatteryState.UIDeviceBatteryStateCharging ||
            UIDevice.currentDevice.batteryState == UIDeviceBatteryState.UIDeviceBatteryStateFull
}
