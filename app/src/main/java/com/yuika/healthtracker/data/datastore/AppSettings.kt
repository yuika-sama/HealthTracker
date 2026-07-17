package com.yuika.healthtracker.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.AppLanguage
import com.yuika.healthtracker.domain.model.AppSettingsState
import com.yuika.healthtracker.domain.model.ThemeColorPreset
import com.yuika.healthtracker.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

object PreferencesKeys
{
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val THEME_COLOR_PRESET = stringPreferencesKey("theme_color_preset")
    val FONT_SIZE = stringPreferencesKey("font_size")
    val LANGUAGE = stringPreferencesKey("language")
    val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
    val TEST_NOTIFICATION_ENABLED = booleanPreferencesKey("test_notification_enabled")
}

@Singleton
class AppSettingsStore @Inject constructor(
    @param:ApplicationContext private val context: Context
)
{
    val settings: Flow<AppSettingsState> = context.dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { preferences ->
            AppSettingsState(
                language = readEnum(preferences[PreferencesKeys.LANGUAGE], AppLanguage.EN),
                themeMode = readEnum(preferences[PreferencesKeys.THEME_MODE], ThemeMode.SYSTEM),
                themeColorPreset = readEnum(
                    preferences[PreferencesKeys.THEME_COLOR_PRESET],
                    ThemeColorPreset.GREEN
                ),
                fontSize = readEnum(preferences[PreferencesKeys.FONT_SIZE], AppFontSize.MEDIUM),
                notificationEnabled = preferences[PreferencesKeys.NOTIFICATION_ENABLED] ?: false,
                testNotificationEnabled = preferences[PreferencesKeys.TEST_NOTIFICATION_ENABLED] ?: false
            )
        }
    private suspend fun save(key: Preferences.Key<String>, value: String){
        context.dataStore.edit { it[key] = value}
    }

    suspend fun setLanguage(value: AppLanguage){
        save(PreferencesKeys.LANGUAGE, value.name)
    }

    suspend fun setThemeMode(value: ThemeMode){
        save(PreferencesKeys.THEME_MODE, value.name)
    }

    suspend fun setThemeColorPreset(value: ThemeColorPreset){
        save(PreferencesKeys.THEME_COLOR_PRESET, value.name)
    }

    suspend fun setFontSize(value: AppFontSize){
        save(PreferencesKeys.FONT_SIZE, value.name)
    }

    suspend fun setNotificationEnabled(value: Boolean){
        context.dataStore.edit { it[PreferencesKeys.NOTIFICATION_ENABLED] = value}
    }

    suspend fun setTestNotificationEnabled(value: Boolean){
        context.dataStore.edit { it[PreferencesKeys.TEST_NOTIFICATION_ENABLED] = value }
    }

    private inline fun <reified T : Enum<T>> readEnum(value: String?, default: T): T
    {
        return value?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: default
    }
}
