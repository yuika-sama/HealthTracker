package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.ui.core.base.UiState
import java.util.UUID

data class AddMealUiState(
    val mealType: String = "",
    val dateText: String = "",

    val foodName: String = "",
    val foodNameError: String? = null,
    val quantity: String = "",
    val quantityError: String? = null,
    val unit: String = "serving",
    val calories: String = "",
    val caloriesError: String? = null,

    val addedFoods: List<TempFoodItem> = emptyList(),
    val totalCalories: Int = 0,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState

data class TempFoodItem(
    val id: String = UUID.randomUUID().toString(),
    val foodName: String,
    val quantity: Float,
    val unit: String,
    val calories: Int
)
