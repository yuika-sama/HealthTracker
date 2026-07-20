package com.yuika.healthtracker.ui.features.main_features.activity

import androidx.annotation.StringRes
import com.yuika.healthtracker.ui.core.base.UiState
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityItemData
import java.time.LocalDate

data class ActivityUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val activities: List<ActivityItemData> = emptyList(),
    val selectedDetail: ActivityItemData? = null,
    val burnedKcal: Int = 0,
    val goalKcal: Int = 0,
    val isLoading: Boolean = false,
    @param:StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState
