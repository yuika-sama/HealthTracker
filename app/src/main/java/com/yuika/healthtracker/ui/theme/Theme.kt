package com.yuika.healthtracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = VitalityGreen,
    secondary = Emerald,
    surface = SurfaceLight,
    surfaceVariant = SurfaceContainerLight,
    error = ErrorRed,
    tertiary = InfoBlue,
    onSurface = OnSurfaceLight
)

private val DarkColorScheme = darkColorScheme(
    primary = VitalityGreen,
    secondary = Emerald,
    surface = SurfaceDark,
    surfaceVariant = SurfaceContainerDark,
    error = ErrorRed,
    tertiary = InfoBlue,
    onSurface = OnSurfaceDark
)

@Composable
fun HealthTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)
{
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}