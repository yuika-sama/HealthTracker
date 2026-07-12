package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class DiaryEffect : UiEffect
{
    data class NavigateToAddFood(val mealType: String): DiaryEffect()
    data class ShowError(val message: String): DiaryEffect()
}