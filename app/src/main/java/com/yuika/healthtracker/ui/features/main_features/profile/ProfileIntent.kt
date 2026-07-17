package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.domain.model.AppFontSize
import com.yuika.healthtracker.domain.model.AppLanguage
import com.yuika.healthtracker.domain.model.ThemeColorPreset
import com.yuika.healthtracker.domain.model.ThemeMode
import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class ProfileIntent : UiIntent{
    object LoadProfile : ProfileIntent()
    object EditProfile : ProfileIntent()

    object LanguageClick: ProfileIntent()
    object ThemeModeClick: ProfileIntent()
    object ThemeColorClick: ProfileIntent()
    object FontSizeClick: ProfileIntent()
    object DismissSettingsDialog: ProfileIntent()

    data class ChangeLanguage(val value: AppLanguage): ProfileIntent()
    data class ChangeThemeMode(val value: ThemeMode):  ProfileIntent()
    data class ChangeThemeColor(val value: ThemeColorPreset):  ProfileIntent()
    data class ChangeFontSize(val value: AppFontSize):  ProfileIntent()
    data class ChangeNotificationEnabled(val value: Boolean): ProfileIntent()

    data class ChangeTestNotificationEnabled(val value: Boolean): ProfileIntent()
}
