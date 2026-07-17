package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class TrendsIntent : UiIntent
{
    object LoadTrendsData : TrendsIntent()
    data class PointClick(val title: String, val point: ChartDataPoint) : TrendsIntent()
    object DismissDetail: TrendsIntent()
    object ExportWeeklyReportClick: TrendsIntent()
//    data class OnPeriodChange(val period: String) : TrendsIntent()
}