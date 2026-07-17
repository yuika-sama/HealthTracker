package com.yuika.healthtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.yuika.healthtracker.data.datastore.AppSettingsStore
import com.yuika.healthtracker.domain.model.AppSettingsState
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.service.pdf_exporter.WeeklyReportScheduler

import com.yuika.healthtracker.ui.navigation.AppNavHost
import com.yuika.healthtracker.ui.theme.HealthTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WeeklyReportScheduler(applicationContext).schedule()
        setContent {
            val appSettingsStore = remember { AppSettingsStore(applicationContext) }
            val settings by appSettingsStore.settings.collectAsStateWithLifecycle(AppSettingsState())

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