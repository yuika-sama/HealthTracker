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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.ThemeColorPreset

private data class Accent(val primary: Color, val secondary: Color, val tertiary: Color)

private fun ThemeColorPreset.accent() = when(this){
    ThemeColorPreset.GREEN -> Accent(VitalityGreen, Emerald, InfoBlue)
    ThemeColorPreset.BLUE -> Accent(AccentBluePrimary, AccentBlueSecondary, EnergyAmber)
    ThemeColorPreset.PURPLE -> Accent(AccentPurpleSecondary, AccentPurpleSecondary, InfoBlue)
    ThemeColorPreset.ORANGE -> Accent(AccentOrangePrimary, AccentOrangeSecondary, InfoBlue)
}

private fun lightScheme(preset: ThemeColorPreset) = preset.accent().let {
    lightColorScheme(
        primary = it.primary,
        secondary = it.secondary,
        tertiary = it.tertiary,
        background = BackgroundLight,
        onBackground = OnSurfaceLight,
        surface = SurfaceLight,
        surfaceVariant = SurfaceContainerLight,
        onSurface = OnSurfaceLight,
        onSurfaceVariant = OnSurfaceVariantLight,
        outline = OutlineLight,
        error = ErrorRed
    )
}

private fun darkScheme(preset: ThemeColorPreset) = preset.accent().let {
    darkColorScheme(
        primary = it.primary,
        secondary = it.secondary,
        tertiary = it.tertiary,
        background = BackgroundDark,
        onBackground = OnSurfaceDark,
        surface = SurfaceDark,
        surfaceVariant = SurfaceContainerDark,
        onSurface = OnSurfaceDark,
        onSurfaceVariant = OnSurfaceVariantDark,
        outline = OutlineDark,
        error = ErrorRed
    )
}

@Composable
fun HealthTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorPreset: ThemeColorPreset= ThemeColorPreset.GREEN,
    fontSize: AppFontSize = AppFontSize.MEDIUM,
    content: @Composable () -> Unit
)
{
    val colorScheme = if (!darkTheme) lightScheme(colorPreset) else darkScheme(colorPreset)

    val density = LocalDensity.current
    val fontScale = when(fontSize){
        AppFontSize.SMALL -> 0.9f
        AppFontSize.MEDIUM -> 1f
        AppFontSize.LARGE -> 1.15f
    }

    CompositionLocalProvider(
        LocalDensity provides Density(density.density, density.fontScale * fontScale)
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}