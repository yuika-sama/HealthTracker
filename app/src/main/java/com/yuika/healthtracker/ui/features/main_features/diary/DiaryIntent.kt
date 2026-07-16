package com.yuika.healthtracker.ui.features.main_features.diary

import com.yuika.healthtracker.ui.core.base.UiIntent
import com.yuika.healthtracker.ui.features.main_features.diary.components.FoodItem
import java.time.LocalDate

sealed class DiaryIntent : UiIntent
{
    object LoadDiaryData: DiaryIntent()
    data class ChangeDate(val date: LocalDate): DiaryIntent()
    data class AddFoodClick(val mealType: String): DiaryIntent()
    data class FoodItemClick(val food: FoodItem): DiaryIntent()

    data class MealClick(val mealType: String): DiaryIntent()

    object DismissDetail: DiaryIntent()
    data class DeleteFoodClick(val foodId: Int): DiaryIntent()
}