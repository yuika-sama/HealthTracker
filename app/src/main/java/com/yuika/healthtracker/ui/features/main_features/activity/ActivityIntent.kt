package com.yuika.healthtracker.ui.features.main_features.activity

import com.yuika.healthtracker.ui.core.base.UiIntent
import java.time.LocalDate

sealed class ActivityIntent : UiIntent
{
    data class LoadActivityData(val date: LocalDate = LocalDate.now()) : ActivityIntent()
    object OnAddActivityClick: ActivityIntent()
}