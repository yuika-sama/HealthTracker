package com.yuika.healthtracker.ui.features.main_features.dashboard

import androidx.lifecycle.viewModelScope
import com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard.GetDashboardDataUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.Job

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
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

    private var fetchJob: Job? = null

    private fun loadDashboardData()
    {
        updateState { it.copy(isLoading = true) }

        val today = LocalDate.now()
        val dbDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val displayFormatter = DateTimeFormatter.ofPattern("MMM, dd")

        val dbDateText = today.format(dbDateFormat)
        val displayDateTime = "Today, ${today.format(displayFormatter)}"

        updateState { it.copy(currentDateText = displayDateTime) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = {throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(DashboardEffect.ShowError(throwable.message ?: "An unexpected error occurred"))
            }
        )
        {
            getDashboardDataUseCase(dbDateText).collectLatest { dashboardData ->
                if (dashboardData != null) {
                    updateState { 
                        it.copy(
                            isLoading = false,
                            userName = dashboardData.userName,
                            intakeCalories = dashboardData.intakeCalories,
                            burnedCalories = dashboardData.burnedCalories,
                            netBalance = dashboardData.netBalance,
                            goalCalories = dashboardData.goalCalories,
                            remainingCalories = dashboardData.remainingCalories,
                            errorMessage = null
                        )
                    }
                } else {
                    updateState { it.copy(isLoading = false, errorMessage = "Unknown user") }
                }
            }
        }
    }
}