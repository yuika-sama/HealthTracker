package com.yuika.healthtracker.ui.features.main_features.profile

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yuika.healthtracker.data.datastore.AppSettingsStore
import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.GetProfileDataUseCase
import com.yuika.healthtracker.service.notification.ReminderWorker
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val appSettingsStore: AppSettingsStore,
    @ApplicationContext private val appContext: Context
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
            is ProfileIntent.Logout -> sendEffect(ProfileEffect.NavigateToLogin)

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
                        notificationEnabled = settings.notificationEnabled
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
            if (enabled) scheduleReminderWorkers() else cancelReminderWorkers()
            appSettingsStore.setNotificationEnabled(enabled)
            updateState { it.copy(errorMessage = null) }
        }
    }

    private fun scheduleReminderWorkers()
    {
        val workManager = WorkManager.getInstance(appContext)

        reminderHours.forEach { hour ->
            val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delayUntil(hour), TimeUnit.MILLISECONDS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                reminderWorkName(hour),
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }
    }

    private fun cancelReminderWorkers()
    {
        val workManager = WorkManager.getInstance(appContext)
        reminderHours.forEach { hour ->
            workManager.cancelUniqueWork(reminderWorkName(hour))
        }
    }

    private fun delayUntil(hour: Int): Long
    {
        val now = LocalDateTime.now()
        var target = now.withHour(hour).withMinute(0).withSecond(0).withNano(0)
        if (!target.isAfter(now)) target = target.plusDays(1)
        return Duration.between(now, target).toMillis()
    }

    private fun reminderWorkName(hour: Int) = "diary_reminder_$hour"

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
                        subtitle = profileData.subtitle,
                        weight = profileData.weight,
                        height = profileData.height,
                        bmi = profileData.bmi,
                        goalTitle = profileData.goalTitle,
                        goalDescription = profileData.goalDescription,
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private companion object
    {
        val reminderHours = listOf(7, 12, 19)
    }

}
