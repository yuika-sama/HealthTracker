package com.yuika.healthtracker.ui.features.main_features.activity

import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.GetActivityDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.ParseActivityIntensityUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getActivityDataUseCase: GetActivityDataUseCase,
    private val parseActivityIntensityUseCase: ParseActivityIntensityUseCase
) : BaseViewModel<ActivityUiState, ActivityIntent, ActivityEffect>(
    initialState = ActivityUiState()
) {
    override fun onIntent(intent: ActivityIntent) {
        when (intent) {
            is ActivityIntent.LoadActivityData -> handleFetchActivity(intent.date)
            is ActivityIntent.OnAddActivityClick -> sendEffect(ActivityEffect.NavigateToAddActivity)
        }
    }

    private var fetchJob: Job? = null

    private fun handleFetchActivity(date: LocalDate) {
        updateState { it.copy(isLoading = true, errorMessage = null, selectedDate = date) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(ActivityEffect.ShowError(throwable.message ?: "Can't get activity data"))
            }
        ) {
            val dateText = date.toString()

            getActivityDataUseCase(dateText).collectLatest { activityData ->
                if (activityData == null) {
                    updateState { it.copy(isLoading = false, errorMessage = "Can't find user data") }
                    sendEffect(ActivityEffect.ShowError("Can't find user data"))
                    return@collectLatest
                }

                val uiActivities = activityData.activities.map { entity ->
                    val intensityLevel = parseActivityIntensityUseCase(entity.intensity)

                    ActivityItemData(
                        title = entity.name,
                        intensity = intensityLevel,
                        durationMins = entity.durationMins,
                        kcal = entity.kcalBurned,
                        iconType = intensityLevel
                    )
                }

                updateState {
                    it.copy(
                        isLoading = false,
                        activities = uiActivities,
                        burnedKcal = activityData.burnedKcal,
                        goalKcal = activityData.goalKcal
                    )
                }
            }
        }
    }
}
