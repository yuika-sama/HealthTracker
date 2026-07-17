package com.yuika.healthtracker.domain.model

data class AppSettingsState(
    val language: AppLanguage = AppLanguage.EN,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val themeColorPreset: ThemeColorPreset = ThemeColorPreset.GREEN,
    val fontSize: AppFontSize = AppFontSize.MEDIUM,
    val notificationEnabled: Boolean = false,
    val testNotificationEnabled: Boolean  = false
)

enum class AppLanguage(val label: String){
    EN("English"),
    VI("Vietnamese")
}

enum class ThemeMode(val label: String){
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System")
}

enum class ThemeColorPreset(val label: String) {
    GREEN("Green"),
    BLUE("Blue"),
    PURPLE("Purple"),
    ORANGE("Orange")
}

enum class AppFontSize(val label: String) {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large")
}