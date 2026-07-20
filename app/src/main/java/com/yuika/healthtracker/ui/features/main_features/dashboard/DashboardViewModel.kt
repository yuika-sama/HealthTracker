package com.yuika.healthtracker.ui.features.main_features.dashboard

import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard.FormatDashboardDateUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard.GetDashboardDataUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
            is DashboardIntent.AddMealClick -> sendEffect(DashboardEffect.NavigateToDiary)
            is DashboardIntent.AddActivityClick -> sendEffect(DashboardEffect.NavigateToActivity)
            is DashboardIntent.SummaryClick -> updateState { it.copy(isBreakdownVisible = true) }
            is DashboardIntent.DismissBreakdown -> updateState { it.copy(isBreakdownVisible = false) }
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
            onError = {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = R.string.error_cannot_get_data,
                        isSuccess = false
                    )
                }
            }
        ) {
            getDashboardDataUseCase(dbDateText).collectLatest { dashboardData ->
                if (dashboardData == null)
                {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessageRes = R.string.error_unknown_user,
                            isSuccess = false
                        )
                    }
                    return@collectLatest
                }
                updateState {
                    it.copy(
                        isLoading = false,
                        userName = dashboardData.userName,
                        intakeCalories = dashboardData.intakeCalories,
                        burnedCalories = dashboardData.burnedCalories,
                        netBalance = dashboardData.netBalance,
                        goalCalories = dashboardData.goalCalories,
                        remainingCalories = dashboardData.remainingCalories,
                        tdeeCalories = dashboardData.tdeeCalories,
                        bmi = dashboardData.bmi,
                        bmiCategory = dashboardData.bmiCategory,
                        errorMessageRes = null,
                        isSuccess = true
                    )
                }
            }
        }
    }

}
