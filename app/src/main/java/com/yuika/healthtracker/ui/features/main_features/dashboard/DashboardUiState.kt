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
    val tdeeCalories: Int = 0,
    val bmi: String  = "",
    val bmiCategory: String = "",
    val isBreakdownVisible: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState {
    val progressFraction: Float
        get() = if (goalCalories > 0) (netBalance / goalCalories.toFloat().coerceIn(0f, 1f)) else 0f

    val adviceText: String
        get() = when {
            goalCalories <= 0 -> "Update your profile to calculate today's target."
            remainingCalories < 0 -> "You are ${-remainingCalories} kcal over target today."
            remainingCalories <= 300 -> "You are close to today's target."
            else -> "You still have $remainingCalories kcal for today"
        }
}
