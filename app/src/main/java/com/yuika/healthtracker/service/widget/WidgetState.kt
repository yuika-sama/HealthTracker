package com.yuika.healthtracker.service.widget

import com.yuika.healthtracker.domain.model.AppSettingsState

data class WidgetState(
    val calories: WidgetCaloriesState,
    val settings: AppSettingsState
)
