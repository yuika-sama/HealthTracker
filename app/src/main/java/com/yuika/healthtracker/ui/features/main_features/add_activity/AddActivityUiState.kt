package com.yuika.healthtracker.ui.features.main_features.add_activity

import com.yuika.healthtracker.ui.core.base.UiState
import com.yuika.healthtracker.ui.core.model.IntensityLevel
import java.time.LocalDate

data class AddActivityUiState(
    val activityName: String = "",
    val activityNameError: String? = null,
    val selectedIcon: String = "run",
    val kcalPerHour: String = "",
    val kcalPerHourError: String? = null,
    val duration: String = "",
    val durationError: String? = null,
    val selectedIntensity: IntensityLevel = IntensityLevel.MEDIUM,
    val dateText: String = LocalDate.now().toString(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState {
    val estimatedKcalBurned: Int
        get(){
            val kcal = kcalPerHour.toIntOrNull() ?: 0
            val mins = duration.toIntOrNull() ?: 0
            return (kcal * mins) / 60
        }
}
