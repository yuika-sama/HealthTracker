package com.yuika.healthtracker

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.yuika.healthtracker.data.datastore.AppSettingsStore
import com.yuika.healthtracker.domain.model.AppLanguage
import com.yuika.healthtracker.domain.model.AppSettingsState
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.service.pdf_exporter.WeeklyReportScheduler

import com.yuika.healthtracker.ui.navigation.AppNavHost
import com.yuika.healthtracker.ui.theme.HealthTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
    override fun attachBaseContext(newBase: Context)
    {
        val language = runBlocking {
            AppSettingsStore(newBase.applicationContext).settings.first().language
        }
        super.attachBaseContext(newBase.withAppLanguage(language))
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WeeklyReportScheduler(applicationContext).schedule()
        setContent {
            val appSettingsStore = remember { AppSettingsStore(applicationContext) }
            val settingsState by remember(appSettingsStore) {
                appSettingsStore.settings.map { it as AppSettingsState? }
            }.collectAsStateWithLifecycle(null)
            val settings = settingsState ?: AppSettingsState()

            LaunchedEffect(settingsState?.language) {
                val language = settingsState?.language ?: return@LaunchedEffect
                if (resources.configuration.locales[0].language != language.localeTag)
                {
                    recreate()
                }
            }

            HealthTrackerTheme(
                darkTheme = when (settings.themeMode)
                {
                    ThemeMode.DARK -> true
                    ThemeMode.LIGHT -> false
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                },
                colorPreset = settings.themeColorPreset,
                fontSize = settings.fontSize
            ) {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController
                )
            }
        }
    }
}

private fun Context.withAppLanguage(language: AppLanguage): Context
{
    val locale = Locale.forLanguageTag(language.localeTag)
    Locale.setDefault(locale)
    val configuration = Configuration(resources.configuration)
    configuration.setLocales(LocaleList(locale))
    return createConfigurationContext(configuration)
}
