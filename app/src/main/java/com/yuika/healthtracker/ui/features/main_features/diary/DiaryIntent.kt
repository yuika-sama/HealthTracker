package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.ui.core.base.UiIntent
import java.time.LocalDate

sealed class DiaryIntent : UiIntent
{
    object LoadDiaryData: DiaryIntent()
    data class ChangeDate(val date: LocalDate): DiaryIntent()
    data class AddFoodClick(val mealType: String): DiaryIntent()
    data class FoodItemClick(val foodName: String): DiaryIntent()
}