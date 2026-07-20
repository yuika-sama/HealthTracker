package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import android.content.Context
import com.yuika.healthtracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yuika.healthtracker.domain.model.FoodEntry
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

data class MealFoodInput(
    val foodName: String,
    val quantity: Float,
    val unit: String,
    val calories: Int,
    val foodCatalogId: Int? = null,
    val caloriesPerServing: Int = calories
)

class ValidateAndSaveMealUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveFoodEntryUseCase: SaveFoodEntryUseCase
) {
    suspend operator fun invoke(
        currentFoods: List<MealFoodInput>,
        dateText: String,
        mealType: String
    ) {
        if (currentFoods.isEmpty()) {
            throw IllegalArgumentException(context.getString(R.string.error_need_one_food))
        }
        val validMealTypes = setOf("Breakfast", "Lunch", "Dinner", "Snack")
        if (mealType !in validMealTypes) {
            throw IllegalArgumentException(context.getString(R.string.error_invalid_meal_type))
        }
        currentFoods.forEach { tempFood ->
            if (tempFood.foodName.isBlank()) {
                throw IllegalArgumentException(context.getString(R.string.error_food_name_blank))
            }
            if (tempFood.quantity <= 0f) {
                throw IllegalArgumentException(context.getString(R.string.error_quantity_positive))
            }
            if (tempFood.unit.isBlank()) {
                throw IllegalArgumentException(context.getString(R.string.error_select_food_unit))
            }
            if (tempFood.calories < 0) {
                throw IllegalArgumentException(context.getString(R.string.error_valid_calories))
            }
        }

        val user = getLatestUserUseCase().firstOrNull()
            ?: throw IllegalStateException(context.getString(R.string.error_cannot_find_user_info))

        currentFoods.forEach { tempFood ->
            val entry = FoodEntry(
                userId = user.id,
                foodCatalogId = tempFood.foodCatalogId,
                dateText = dateText,
                mealType = mealType,
                foodName = tempFood.foodName,
                quantity = tempFood.quantity,
                unit = tempFood.unit,
                calories = tempFood.calories,
                caloriesPerServing = tempFood.caloriesPerServing,
                imagePath = null
            )
            saveFoodEntryUseCase(entry)
        }
    }
}
