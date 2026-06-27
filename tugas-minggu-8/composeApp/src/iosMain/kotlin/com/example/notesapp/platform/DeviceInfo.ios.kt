package com.example.notesapp.platform

import platform.UIKit.UIDevice

actual class DeviceInfo actual constructor() {
    actual fun getDeviceName(): String = UIDevice.currentDevice.name
    actual fun getOsVersion(): String =
        "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"
    actual fun getAppVersion(): String = "1.0.0"
}
