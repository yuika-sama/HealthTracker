package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.MealFoodInput
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

    val addedFoods: List<AddedFoodItem> = emptyList(),
    val totalCalories: Int = 0,

    val isManual: Boolean = false,
    val selectedFoodCatalogId: Int? = null,
    val selectedCategoriesPerServing: Int = 0,
    val selectedDefaultQuantity: Float = 1f,
    val searchResults: List<FoodCatalog> = emptyList(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState

data class AddedFoodItem(
    val id: String = UUID.randomUUID().toString(),
    val foodName: String,
    val quantity: Float,
    val unit: String,
    val calories: Int,
    val foodCatalogId: Int? = null,
    val caloriesPerServing: Int = calories
) {
    fun toMealFoodInput() = MealFoodInput(
        foodName = foodName,
        quantity = quantity,
        unit = unit,
        calories = calories,
        foodCatalogId = foodCatalogId,
        caloriesPerServing = caloriesPerServing
    )
}
