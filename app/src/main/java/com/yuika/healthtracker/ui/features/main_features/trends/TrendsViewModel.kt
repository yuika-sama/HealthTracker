package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.usecase.main_use_cases.trends.GetTrendsDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.trends.TrendsChartDataPoint
import com.yuika.healthtracker.service.pdf_exporter.WeeklyReportService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.time.LocalDate
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
                handleFetchTrends(state.value.startDate, state.value.endDate)
            }

            is TrendsIntent.ChangeDateRange -> handleDateRangeChange(
                intent.startDate,
                intent.endDate
            )

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

    private fun handleDateRangeChange(startDate: LocalDate, endDate: LocalDate)
    {
        val start = minOf(startDate, endDate)
        val end = maxOf(startDate, endDate)
        updateState { it.copy(startDate = start, endDate = end, selectedDetail = null) }
        handleFetchTrends(start, end)
    }

    private fun exportWeeklyReport()
    {
        launchSafe(
            onError = {
                updateState {
                    it.copy(
                        isExportingReport = false,
                        errorMessageRes = R.string.error_export_report
                    )
                }
            }
        ) {
            updateState { it.copy(isExportingReport = true, errorMessageRes = null) }
            val uri = weeklyReportService.exportRange(state.value.startDate, state.value.endDate)
            updateState { it.copy(isExportingReport = false) }
            sendEffect(TrendsEffect.ShareWeeklyReport(uri))
        }
    }

    private var fetchJob: Job? = null

    private fun handleFetchTrends(startDate: LocalDate, endDate: LocalDate)
    {
        updateState { it.copy(isLoading = true, errorMessageRes = null, isSuccess = false) }

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
            getTrendsDataUseCase(startDate, endDate).collectLatest { trendsData ->
                if (trendsData == null)
                {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessageRes = R.string.error_cannot_find_user_info
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
                        errorMessageRes = null
                    )
                }
            }
        }
    }
}
