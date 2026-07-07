package com.yuika.healthtracker.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

object PreferencesKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val FONT_SIZE = stringPreferencesKey("font_size")
    val LANGUAGE = stringPreferencesKey("language")
    val NOTIFICATION_ENABLED = stringPreferencesKey("notification_enabled")
}