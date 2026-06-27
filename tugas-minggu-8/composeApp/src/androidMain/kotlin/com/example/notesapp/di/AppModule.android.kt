package com.example.notesapp.di

import android.content.Context
import com.example.notesapp.data.db.DatabaseDriverFactory
import com.example.notesapp.platform.BatteryInfo
import com.example.notesapp.platform.DeviceInfo
import com.example.notesapp.platform.NetworkMonitor
import com.notes.app.db.NotesDatabase
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory(androidContext()) }
    single { NotesDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences("notes_settings", Context.MODE_PRIVATE)
        )
    }
    single { DeviceInfo() }
    single { BatteryInfo() }
    single { NetworkMonitor(androidContext()) }
}
