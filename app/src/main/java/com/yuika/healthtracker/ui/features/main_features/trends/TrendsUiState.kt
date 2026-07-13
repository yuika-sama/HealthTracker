package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.ui.core.base.UiState

data class ChartDataPoint(
    val label: String,
    val value: Float
)

data class TrendsUiState(
    val selectedPeriod: String = "Week",

    val avgIntake: String = "0",
    val avgBurned: String = "0",
    val daysMeetingGoal: String = "0",
    val goalDays: String = " / 7 days",

    val intakeChartData: List<ChartDataPoint> = emptyList(),
    val netCaloriesChartData: List<ChartDataPoint> = emptyList(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState
