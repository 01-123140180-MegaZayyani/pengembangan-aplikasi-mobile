package com.example.notesapp.data.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class SettingsManager(private val settings: Settings) {

    companion object {
        private const val KEY_THEME       = "app_theme"
        private const val KEY_SORT_ORDER  = "sort_order"
        private const val KEY_USER_NAME   = "user_name"
        private const val KEY_VIEW_STYLE  = "view_style"
    }

    var theme: String
        get() = settings[KEY_THEME, "system"]
        set(value) { settings[KEY_THEME] = value }

    var sortOrder: String
        get() = settings[KEY_SORT_ORDER, "newest"]
        set(value) { settings[KEY_SORT_ORDER] = value }

    var userName: String
        get() = settings[KEY_USER_NAME, "Notes User"]
        set(value) { settings[KEY_USER_NAME] = value }

    var viewStyle: String
        get() = settings[KEY_VIEW_STYLE, "list"]
        set(value) { settings[KEY_VIEW_STYLE] = value }
}
