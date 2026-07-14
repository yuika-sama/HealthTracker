package com.yuika.healthtracker.ui.features.main_features.dashboard

import com.yuika.healthtracker.ui.core.base.UiState

data class DashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val currentDateText: String = "",
    val intakeCalories: Int = 0,
    val burnedCalories: Int = 0,
    val netBalance: Int = 0,
    val goalCalories: Int = 0,
    val remainingCalories: Int = 0,
    val errorMessage: String? = null
) : UiState
