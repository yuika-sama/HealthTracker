package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class DiaryEffect : UiEffect
{
    data class NavigateToAddFood(val mealType: String, val dateText: String): DiaryEffect()
}
