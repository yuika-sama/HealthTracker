package com.yuika.healthtracker.ui.theme

import androidx.compose.ui.graphics.Color
import com.yuika.healthtracker.domain.model.ThemeColorPreset

data class AppAccentColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color
)

fun ThemeColorPreset.accentColors(): AppAccentColors = when(this){
    ThemeColorPreset.GREEN -> AppAccentColors(VitalityGreen, Emerald, InfoBlue)
    ThemeColorPreset.BLUE -> AppAccentColors(AccentBluePrimary, AccentBlueSecondary, EnergyAmber)
    ThemeColorPreset.PURPLE -> AppAccentColors(AccentPurplePrimary, AccentPurpleSecondary, InfoBlue)
    ThemeColorPreset.ORANGE -> AppAccentColors(AccentOrangePrimary, AccentOrangeSecondary, InfoBlue)
}
