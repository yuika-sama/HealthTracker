package com.yuika.healthtracker.ui.features.main_features.dashboard

import androidx.annotation.StringRes
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
    val tdeeCalories: Int = 0,
    val bmi: String  = "",
    val bmiCategory: String = "",
    val isBreakdownVisible: Boolean = false,
    @param:StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState {
    val progressFraction: Float
        get() = if (goalCalories > 0) (netBalance / goalCalories.toFloat()).coerceIn(0f, 1f) else 0f
}
