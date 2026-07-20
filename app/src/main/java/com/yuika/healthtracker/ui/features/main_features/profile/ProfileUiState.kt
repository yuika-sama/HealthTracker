package com.yuika.healthtracker.ui.features.main_features.profile

import androidx.annotation.StringRes
import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.AppLanguage
import com.yuika.healthtracker.domain.model.ThemeColorPreset
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.ui.core.base.UiState

data class ProfileUiState(
    val name: String = "",
    val activityLevel: String = "",
    val weight: String = "",
    val height: String = "",
    val bmi: String = "",
    val bmiCategory: String = "",
    val avatarPath: String? = null,
    val goal: String = "",
    val goalCalories: Int = 0,
    val language: AppLanguage = AppLanguage.EN,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val themeColorPreset: ThemeColorPreset = ThemeColorPreset.GREEN,
    val fontSize: AppFontSize = AppFontSize.MEDIUM,
    val notificationEnabled: Boolean = false,
    val testNotificationEnabled: Boolean = false,
    val activeSettingsDialog: ProfileSettingsDialog? = null,
    val isLoading: Boolean = false,
    @StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState {
    val bmiText get() = "$bmi ($bmiCategory)"
}

enum class ProfileSettingsDialog{
    LANGUAGE,
    THEME_MODE,
    THEME_COLOR,
    FONT_SIZE
}
