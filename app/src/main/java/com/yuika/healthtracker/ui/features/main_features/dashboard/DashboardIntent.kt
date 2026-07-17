package com.yuika.healthtracker.ui.features.main_features.dashboard

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class DashboardIntent : UiIntent
{
    object LoadDashboardData: DashboardIntent()
    object AddMealClick: DashboardIntent()
    object AddActivityClick: DashboardIntent()
    object SummaryClick: DashboardIntent()
    object DismissBreakdown : DashboardIntent()
}