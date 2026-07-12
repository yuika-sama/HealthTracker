package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.ui.core.base.UiState
import java.util.UUID

data class AddMealUiState(
    val mealType: String = "",
    val dateText: String = "",

    val foodName: String = "",
    val quantity: String = "",
    val unit: String = "serving",
    val calories: String = "",

    val addedFoods: List<TempFoodItem> = emptyList(),
    val totalCalories: Int = 0,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : UiState

data class TempFoodItem(
    val id: String = UUID.randomUUID().toString(),
    val foodName: String,
    val quantity: Float,
    val unit: String,
    val calories: Int
)
