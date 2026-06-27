package com.example.notesapp.di

import com.example.notesapp.data.db.DatabaseDriverFactory
import com.example.notesapp.platform.BatteryInfo
import com.example.notesapp.platform.DeviceInfo
import com.example.notesapp.platform.NetworkMonitor
import com.notes.app.db.NotesDatabase
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual val platformModule: Module = module {
    single { DatabaseDriverFactory() }
    single { NotesDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single<Settings> { NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) }
    single { DeviceInfo() }
    single { BatteryInfo() }
    single { NetworkMonitor() }
}
