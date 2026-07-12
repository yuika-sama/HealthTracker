package com.yuika.healthtracker.ui.features.main_features.add_meal

import android.health.connect.datatypes.MealType
import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class AddMealIntent : UiIntent
{
    data class Init(val mealType: String, val dateText: String): AddMealIntent()
    data class OnFoodNameChange(val name: String): AddMealIntent()
    data class OnQuantityChange(val quantity: String): AddMealIntent()
    data class OnUnitChange(val unit: String): AddMealIntent()
    data class OnCaloriesChange(val calories: String): AddMealIntent()

    data class OnMealTypeChange(val mealType: String) : AddMealIntent()

    object OnAddFoodClick: AddMealIntent()
    data class OnRemoveFoodClick(val id: String): AddMealIntent()

    object OnSaveMealClick: AddMealIntent()
}