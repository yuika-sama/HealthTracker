package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.AppLanguage
import com.yuika.healthtracker.domain.model.ThemeColorPreset
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.ui.core.base.UiState

data class ProfileUiState(
    val name: String = "",
    val subtitle: String = "",
    val weight: String = "",
    val height: String = "",
    val bmi: String = "",
    val goalTitle: String = "Current Goal",
    val goalDescription: String = "",
    val language: AppLanguage = AppLanguage.EN,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val themeColorPreset: ThemeColorPreset = ThemeColorPreset.GREEN,
    val fontSize: AppFontSize = AppFontSize.MEDIUM,
    val notificationEnabled: Boolean = false,
    val activeSettingsDialog: ProfileSettingsDialog? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState {
    val languageLabel get() = language.label
    val themeModeLabel get() = themeMode.label
    val themeColorLabel get() = themeColorPreset.label
    val fontSizeLabel get() = fontSize.label
}

enum class ProfileSettingsDialog{
    LANGUAGE,
    THEME_MODE,
    THEME_COLOR,
    FONT_SIZE
}
