package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.ui.core.base.UiState
import java.time.LocalDate

data class ChartDataPoint(
    val label: String,
    val value: Float,
    val dateText: String = "",
    val intake: Int = 0,
    val burned: Int = 0
)
{
    val balance: Int get() = intake - burned
}

data class TrendDetail(
    val title: String,
    val label: String,
    val dateText: String,
    val intake: Int,
    val burned: Int
)
{
    val balance: Int get() = intake - burned
}

data class TrendsUiState(
    val startDate: LocalDate = LocalDate.now().minusDays(6),
    val endDate: LocalDate = LocalDate.now(),

    val avgIntake: String = "0",
    val avgBurned: String = "0",
    val daysMeetingGoal: String = "0",
    val goalDays: String = "7",

    val intakeChartData: List<ChartDataPoint> = emptyList(),
    val weeklyTrendChartData: List<ChartDataPoint> = emptyList(),
    val selectedDetail: TrendDetail? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val isExportingReport: Boolean = false
) : UiState
