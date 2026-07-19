package com.yuika.healthtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.ThemeColorPreset

@Composable
fun HealthTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorPreset: ThemeColorPreset= ThemeColorPreset.GREEN,
    fontSize: AppFontSize = AppFontSize.MEDIUM,
    content: @Composable () -> Unit
)
{
    val accent = colorPreset.accentColors()
    val colorScheme = if (!darkTheme) {
        lightColorScheme(
            primary = accent.primary,
            secondary = accent.secondary,
            tertiary = accent.tertiary,
            background = BackgroundLight,
            onBackground = OnSurfaceLight,
            surface = SurfaceLight,
            surfaceVariant = SurfaceContainerLight,
            onSurface = OnSurfaceLight,
            onSurfaceVariant = OnSurfaceVariantLight,
            outline = OutlineLight,
            error = ErrorRed
        )
    } else {
        darkColorScheme(
            primary = accent.primary,
            secondary = accent.secondary,
            tertiary = accent.tertiary,
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
