package com.yuika.healthtracker.ui.features.main_features.dashboard

import com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard.FormatDashboardDateUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard.GetDashboardDataUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val formatDashboardDateUseCase: FormatDashboardDateUseCase
) : BaseViewModel<DashboardUiState, DashboardIntent, DashboardEffect>(
    initialState = DashboardUiState()
)
{
    override fun onIntent(intent: DashboardIntent)
    {
        when (intent)
        {
            is DashboardIntent.LoadDashboardData -> loadDashboardData()
            is DashboardIntent.AddMealClick -> navigateWithLoading(DashboardEffect.NavigateToDiary)
            is DashboardIntent.AddActivityClick -> navigateWithLoading(DashboardEffect.NavigateToActivity)
        }
    }

    private var fetchJob: Job? = null

    private fun loadDashboardData()
    {
        updateState { it.copy(isLoading = true) }

        val (dbDateText, displayDateTime) = formatDashboardDateUseCase()

        updateState { it.copy(currentDateText = displayDateTime) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "An unexpected error occurred"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
                sendEffect(DashboardEffect.ShowError(message))
            }
        ) {
            getDashboardDataUseCase(dbDateText).collectLatest { dashboardData ->
                if (dashboardData == null)
                {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Unknown user",
                            isSuccess = false
                        )
                    }
                    sendEffect(DashboardEffect.ShowError("Unknown user"))
                    return@collectLatest
                }
                delay(NETWORK_DELAY.toLong())
                updateState {
                    it.copy(
                        isLoading = false,
                        userName = dashboardData.userName,
                        intakeCalories = dashboardData.intakeCalories,
                        burnedCalories = dashboardData.burnedCalories,
                        netBalance = dashboardData.netBalance,
                        goalCalories = dashboardData.goalCalories,
                        remainingCalories = dashboardData.remainingCalories,
                        errorMessage = null,
                        isSuccess = true
                    )
                }
            }
        }
    }

    private fun navigateWithLoading(effect: DashboardEffect)
    {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Unknown error occurred"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
                sendEffect(DashboardEffect.ShowError(message))
            }
        ) {
            delay(NETWORK_DELAY.toLong())
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(effect)
        }
    }
}
