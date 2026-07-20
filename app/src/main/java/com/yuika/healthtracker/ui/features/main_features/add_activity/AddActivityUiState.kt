package com.yuika.healthtracker.ui.features.main_features.add_activity

import androidx.annotation.StringRes
import com.yuika.healthtracker.domain.model.ActivityCatalog
import com.yuika.healthtracker.ui.core.base.UiState
import java.time.LocalDate
import kotlin.math.roundToInt

data class AddActivityUiState(
    val dateText: String = LocalDate.now().toString(),
    val activityCatalogs: List<ActivityCatalog> = emptyList(),
    val selectedActivity: ActivityCatalog? = null,
    @param:StringRes val activityCatalogErrorRes: Int? = null,
    val duration: String = "",
    @param:StringRes val durationErrorRes: Int? = null,
    val userWeightKg: Double = 0.0,
    val isLoading: Boolean = false,
    @param:StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState
{
    val estimatedKcalBurned: Int
        get() = ((selectedActivity?.met ?: 0.0) * userWeightKg * ((duration.toIntOrNull()
            ?: 0) / 60.0)).roundToInt()
}
