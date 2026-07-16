package com.yuika.healthtracker.ui.features.main_features.activity

import com.yuika.healthtracker.ui.core.base.UiIntent
import com.yuika.healthtracker.ui.features.main_features.activity.components.ActivityItemData
import java.time.LocalDate

sealed class ActivityIntent : UiIntent
{
    data class LoadActivityData(val date: LocalDate = LocalDate.now()) : ActivityIntent()
    object OnAddActivityClick: ActivityIntent()
    data class ActivityClick(val activity: ActivityItemData) : ActivityIntent()
    object DismissDetail : ActivityIntent()
    data class DeleteActivityClick(val id: Int) : ActivityIntent()
}
