package com.yuika.healthtracker.ui.features.main_features.activity

import com.yuika.healthtracker.domain.repository.ActivityRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityItem
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityItemData
import com.yuika.healthtracker.ui.features.main_features.activity.components.IntensityLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val activityRepository: ActivityRepository
) : BaseViewModel<ActivityUiState, ActivityIntent, ActivityEffect>(
    initialState = ActivityUiState()
)
{
    override fun onIntent(intent: ActivityIntent)
    {
        when (intent) {
            is ActivityIntent.LoadActivityData ->
            {
                handleFetchActivity(intent.date)
            }
            is ActivityIntent.OnAddActivityClick -> {
                sendEffect(ActivityEffect.NavigateToAddActivity)
            }

        }
    }

    private fun handleFetchActivity(date: LocalDate){
        updateState { it.copy(isLoading = true, errorMessage = null, selectedDate = date) }

        launchSafe(
            onError = {throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(ActivityEffect.ShowError(throwable.message ?: "Can't get activity data"))            }
        )
        {
            val user = userRepository.getLatestUserFlow().firstOrNull()
            if (user == null){
                updateState { it.copy(isLoading = false, errorMessage = "Can't find user data") }
                sendEffect(ActivityEffect.ShowError("Can't find user data"))
                return@launchSafe
            }

            val dateText = date.toString()
            val activityEntity = activityRepository.getActivitiesByDate(user.id, dateText = dateText).firstOrNull() ?: emptyList()

            val totalBurned = activityEntity.sumOf{it.kcalBurned}

            val uiActivities = activityEntity.map { entity ->
                val intensityLevel = try {
                    IntensityLevel.valueOf(entity.intensity.uppercase())
                } catch (e: Exception) {
                    IntensityLevel.LOW
                }

                ActivityItemData(
                    title =  entity.name,
                    intensity = intensityLevel,
                    durationMins = entity.durationMins,
                    kcal = entity.kcalBurned,
                    iconType = intensityLevel
                )
            }

            // todo: user real goal kcal in repository
            val goalKCal = 0

            updateState {
                it.copy(
                    isLoading = false,
                    activities = uiActivities,
                    burnedKcal = totalBurned,
                    goalKcal = goalKCal
                )
            }
        }
    }
}

