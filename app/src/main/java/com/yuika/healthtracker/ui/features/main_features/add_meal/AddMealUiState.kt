package com.yuika.healthtracker.ui.features.main_features.add_meal

import androidx.annotation.StringRes
import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.MealFoodInput
import com.yuika.healthtracker.ui.core.base.UiState
import java.util.UUID

data class AddMealUiState(
    val mealType: String = "",
    val dateText: String = "",

    val foodName: String = "",
    @param:StringRes val foodNameErrorRes: Int? = null,
    val quantity: String = "",
    @param:StringRes val quantityErrorRes: Int? = null,
    val unit: String = "serving",
    val calories: String = "",
    @param:StringRes val caloriesErrorRes: Int? = null,

    val addedFoods: List<AddedFoodItem> = emptyList(),
    val totalCalories: Int = 0,

    val isManual: Boolean = false,
    val selectedFoodCatalogId: Int? = null,
    val selectedCategoriesPerServing: Int = 0,
    val selectedDefaultQuantity: Float = 1f,
    val searchResults: List<FoodCatalog> = emptyList(),

    val isLoading: Boolean = false,
    @param:StringRes val errorMessageRes: Int? = null,
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
