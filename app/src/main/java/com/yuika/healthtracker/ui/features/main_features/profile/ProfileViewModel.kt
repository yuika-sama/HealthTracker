package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.data.datastore.AppSettingsStore
import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.GetProfileDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.UpdateUserAvatarUseCase
import com.yuika.healthtracker.service.notification.ReminderNotificationService
import com.yuika.healthtracker.service.widget.WidgetService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val updateUserAvatarUseCase: UpdateUserAvatarUseCase,
    private val appSettingsStore: AppSettingsStore,
    private val reminderNotificationService: ReminderNotificationService,
    private val widgetService: WidgetService
) : BaseViewModel<ProfileUiState, ProfileIntent, ProfileEffect>(
    initialState = ProfileUiState()
)
{

    init {
        observeSettings()
    }

    override fun onIntent(intent: ProfileIntent)
    {
        when (intent)
        {
            is ProfileIntent.LoadProfile -> handleLoadProfile()
            is ProfileIntent.EditProfile -> sendEffect(ProfileEffect.NavigateToEditProfile)

            is ProfileIntent.LanguageClick -> showDialog(ProfileSettingsDialog.LANGUAGE)
            is ProfileIntent.ThemeModeClick -> showDialog(ProfileSettingsDialog.THEME_MODE)
            is ProfileIntent.ThemeColorClick -> showDialog(ProfileSettingsDialog.THEME_COLOR)
            is ProfileIntent.FontSizeClick -> showDialog(ProfileSettingsDialog.FONT_SIZE)
            is ProfileIntent.DismissSettingsDialog -> showDialog(null)

            is ProfileIntent.ChangeLanguage -> saveSetting { appSettingsStore.setLanguage(intent.value) }
            is ProfileIntent.ChangeThemeMode -> saveSetting { appSettingsStore.setThemeMode(intent.value) }
            is ProfileIntent.ChangeThemeColor -> saveSetting { appSettingsStore.setThemeColorPreset(intent.value) }
            is ProfileIntent.ChangeFontSize -> saveSetting { appSettingsStore.setFontSize(intent.value) }
            is ProfileIntent.ChangeNotificationEnabled -> changeNotificationEnabled(intent.value)
            is ProfileIntent.ChangeTestNotificationEnabled -> changeTestNotificationEnabled(intent.value)
            is ProfileIntent.SaveAvatar -> saveAvatar(intent.avatarPath)
        }
    }

    private fun observeSettings() {
        launchSafe {
            appSettingsStore.settings.collectLatest { settings ->
                updateState {
                    it.copy(
                        language = settings.language,
                        themeMode = settings.themeMode,
                        themeColorPreset = settings.themeColorPreset,
                        fontSize = settings.fontSize,
                        notificationEnabled = settings.notificationEnabled,
                        testNotificationEnabled = settings.testNotificationEnabled
                    )
                }
            }
        }
    }

    private fun showDialog(dialog: ProfileSettingsDialog?){
        updateState { it.copy(activeSettingsDialog = dialog) }
    }

    private fun saveSetting(block: suspend () -> Unit){
        launchSafe(
            onError = {error ->
                updateState { it.copy(errorMessage = error.message ?: "Can't save settings") }
            }
        ) {
            block()
            widgetService.refresh()
            updateState { it.copy(activeSettingsDialog = null, errorMessage = null) }
        }
    }

    private fun changeNotificationEnabled(enabled: Boolean)
    {
        launchSafe(
            onError = { error ->
                updateState { it.copy(errorMessage = error.message ?: "Can't save notification settings") }
            }
        ) {
            appSettingsStore.setNotificationEnabled(enabled)
            reminderNotificationService.setDailyReminderEnabled(enabled)
            updateState { it.copy(errorMessage = null) }
        }
    }

    private fun changeTestNotificationEnabled(enabled: Boolean)
    {
        launchSafe(
            onError = { error ->
                updateState { it.copy(errorMessage = error.message ?: "Can't save test notification settings") }
            }
        ) {
            appSettingsStore.setTestNotificationEnabled(enabled)
            reminderNotificationService.setTestReminderEnabled(enabled)
            updateState { it.copy(errorMessage = null) }
        }
    }

    private fun saveAvatar(avatarPath: String)
    {
        launchSafe(
            onError = { error ->
                updateState { it.copy(errorMessage = error.message ?: "Can't save avatar") }
            }
        ) {
            val path = avatarPath.trim()
            updateUserAvatarUseCase(path)
            updateState { it.copy(avatarPath = path, errorMessage = null, isSuccess = true) }
        }
    }

    private var fetchJob: Job? = null

    private fun handleLoadProfile()
    {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Error loading information"
                updateState { it.copy(isLoading = false, errorMessage = message) }
            }
        ) {
            getProfileDataUseCase().collectLatest { profileData ->
                if (profileData == null)
                {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Can't find user information",
                            isSuccess = false
                        )
                    }
                    return@collectLatest
                }

                delay(NETWORK_DELAY.toLong())

                updateState {
                    it.copy(
                        name = profileData.name,
                        activityLevel = profileData.activityLevel,
                        weight = profileData.weight,
                        height = profileData.height,
                        bmi = profileData.bmi,
                        bmiCategory = profileData.bmiCategory,
                        avatarPath = profileData.avatarPath,
                        goal = profileData.goal,
                        goalCalories = profileData.goalCalories,
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
            }
        }
    }
}
