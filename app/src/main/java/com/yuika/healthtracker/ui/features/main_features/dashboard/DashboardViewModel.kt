package com.yuika.healthtracker.ui.features.main_features.dashboard

import android.icu.util.LocaleData
import androidx.lifecycle.viewModelScope
import com.yuika.healthtracker.domain.repository.ActivityRepository
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val foodEntryRepository: FoodEntryRepository,
    private val activityRepository: ActivityRepository
) : BaseViewModel<DashboardUiState, DashboardIntent, DashboardEffect>(
    initialState = DashboardUiState()
)
{
    override fun onIntent(intent: DashboardIntent)
    {
        when (intent){
            is DashboardIntent.LoadDashboardData -> loadDashboardData()
            is DashboardIntent.AddMealClick -> sendEffect(DashboardEffect.NavigateToDiary)
            is DashboardIntent.AddActivityClick -> sendEffect(DashboardEffect.NavigateToActivity)
        }
    }

    private fun loadDashboardData()
    {
        updateState { it.copy(isLoading = true) }

        val today = LocalDate.now()
        val dbDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val displayFormatter = DateTimeFormatter.ofPattern("MMM, dd")

        val dbDateText = today.format(dbDateFormat)
        val displayDateTime = "Today, ${dbDateText.format(displayFormatter)}"

        updateState { it.copy(currentDateText = displayDateTime) }

        launchSafe(
            onError = {throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(DashboardEffect.ShowError(throwable.message ?: "An unexpected error occurred"))
            }
        )
        {
            userRepository.getLatestUserFlow().collectLatest { user ->
                if (user != null) {
                    updateState { it.copy(userName = user.name) }

                    observeStats(user.id, dbDateText)
                } else {
                    updateState {
                        it.copy(isLoading = false, errorMessage = "Unknown user")
                    }
                }
            }
        }
    }

    private fun observeStats(id: Int, dbDateText: String)
    {
        viewModelScope.launch {
            val intakeFlow = foodEntryRepository.getTotalCaloriesByDate(id, dbDateText)
            val burnedFlow = activityRepository.getTotalBurnedCaloriesByDate(id, dbDateText)

            combine(intakeFlow, burnedFlow) { intake, burned ->
                val safeIntake = intake ?: 0
                val safeBurned = burned ?: 0
                val net = safeIntake - safeBurned

                val goal = 2000
                val remaining = goal - net

                DashboardUiState(
                    isLoading = false,
                    userName = state.value.userName,
                    currentDateText = state.value.currentDateText,
                    intakeCalories = safeIntake,
                    burnedCalories = safeBurned,
                    netBalance = net,
                    goalCalories = goal,
                    remainingCalories = remaining,
                    errorMessage = null
                )
            }.collectLatest { newState ->
                updateState {
                    it.copy(
                        isLoading = false,
                        intakeCalories = newState.intakeCalories,
                        burnedCalories = newState.burnedCalories,
                        netBalance = newState.netBalance,
                        goalCalories = newState.goalCalories,
                        remainingCalories = newState.remainingCalories
                    )
                }
            }
        }
    }
}