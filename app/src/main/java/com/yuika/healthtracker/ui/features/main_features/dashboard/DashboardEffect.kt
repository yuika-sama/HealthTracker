package com.yuika.healthtracker.ui.features.main_features.dashboard

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class DashboardEffect : UiEffect
{
    object NavigateToDiary: DashboardEffect()
    object NavigateToActivity: DashboardEffect()
    data class ShowError(val message: String): DashboardEffect()
}