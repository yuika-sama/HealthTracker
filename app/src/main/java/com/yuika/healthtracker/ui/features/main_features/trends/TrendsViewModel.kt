package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.domain.usecase.main_use_cases.trends.GetTrendsDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.trends.TrendsChartDataPoint
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val getTrendsDataUseCase: GetTrendsDataUseCase
) : BaseViewModel<TrendsUiState, TrendsIntent, TrendsEffect>(
    initialState = TrendsUiState()
)
{
    override fun onIntent(intent: TrendsIntent)
    {
        when (intent)
        {
            is TrendsIntent.LoadTrendsData ->
            {
                handleFetchTrends(state.value.selectedPeriod)
            }

            is TrendsIntent.OnPeriodChange ->
            {
                updateState { it.copy(selectedPeriod = intent.period) }
                handleFetchTrends(intent.period)
            }
        }
    }

    private var fetchJob: Job? = null

    private fun handleFetchTrends(period: String)
    {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(TrendsEffect.ShowError(throwable.message ?: "Can't get data"))
            }
        ) {
            getTrendsDataUseCase(period).collectLatest { trendsData ->
                if (trendsData == null) {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Can't find user information"
                        )
                    }
                    sendEffect(TrendsEffect.ShowError("Can't find user information"))
                    return@collectLatest
                }

                fun List<TrendsChartDataPoint>.toUiModels() = this.map {
                    ChartDataPoint(label = it.label, value = it.value)
                }

                updateState {
                    it.copy(
                        avgIntake = trendsData.avgIntakeStr,
                        avgBurned = trendsData.avgBurnedStr,
                        daysMeetingGoal = trendsData.daysMeetingGoal,
                        goalDays = trendsData.goalDays,
                        intakeChartData = trendsData.intakeChartData.toUiModels(),
                        netCaloriesChartData = trendsData.netCaloriesChartData.toUiModels(),
                        isLoading = false
                    )
                }
            }
        }
    }
}