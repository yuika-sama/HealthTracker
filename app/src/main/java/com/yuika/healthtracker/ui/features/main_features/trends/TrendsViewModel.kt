package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.domain.usecase.main_use_cases.trends.GetTrendsDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.trends.TrendsChartDataPoint
import com.yuika.healthtracker.service.pdf_exporter.WeeklyReportService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class TrendsViewModel @Inject constructor(
    private val getTrendsDataUseCase: GetTrendsDataUseCase,
    private val weeklyReportService: WeeklyReportService
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
                handleFetchTrends()
            }

            is TrendsIntent.PointClick -> updateState {
                it.copy(
                    selectedDetail = TrendDetail(
                        title = intent.title,
                        label = intent.point.label,
                        dateText = intent.point.dateText,
                        intake = intent.point.intake,
                        burned = intent.point.burned
                    )
                )
            }

            is TrendsIntent.DismissDetail -> updateState { it.copy(selectedDetail = null) }

            is TrendsIntent.ExportWeeklyReportClick -> exportWeeklyReport()
        }
    }

    private fun exportWeeklyReport()
    {
        launchSafe(
            onError = {error ->
                updateState {
                    it.copy(
                        isExportingReport = false,
                        errorMessage = error.message ?: "Can't export weekly report"
                    )
                }
            }
        ) {
            updateState { it.copy(isExportingReport = true, errorMessage = null) }
            val uri = weeklyReportService.exportCurrentWeek()
            updateState { it.copy(isExportingReport = false) }
            sendEffect(TrendsEffect.ShareWeeklyReport(uri))
        }
    }

    private var fetchJob: Job? = null

    private fun handleFetchTrends()
    {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        fetchJob?.cancel()
        fetchJob = launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Can't get data"
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = message,
                        isSuccess = false
                    )
                }
            }
        ) {
            getTrendsDataUseCase().collectLatest { trendsData ->
                if (trendsData == null)
                {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Can't find user information"
                        )
                    }
                    return@collectLatest
                }

                fun List<TrendsChartDataPoint>.toUiModels() = map {
                    ChartDataPoint(it.label, it.value, it.dateText, it.intake, it.burned)
                }

                delay(NETWORK_DELAY.toLong())

                updateState {
                    it.copy(
                        avgIntake = trendsData.avgIntakeStr,
                        avgBurned = trendsData.avgBurnedStr,
                        daysMeetingGoal = trendsData.daysMeetingGoal,
                        goalDays = trendsData.goalDays,
                        intakeChartData = trendsData.intakeChartData.toUiModels(),
                        weeklyTrendChartData = trendsData.weeklyTrendChartData.toUiModels(),
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
            }
        }
    }
}
