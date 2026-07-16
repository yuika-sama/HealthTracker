package com.yuika.healthtracker.domain.usecase.main_use_cases.food

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
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveFoodEntryUseCase: SaveFoodEntryUseCase
) {
    suspend operator fun invoke(
        currentFoods: List<MealFoodInput>,
        dateText: String,
        mealType: String
    ) {
        if (currentFoods.isEmpty()) {
            throw IllegalArgumentException("Please at least add one food in the meal")
        }
        val validMealTypes = setOf("Breakfast", "Lunch", "Dinner", "Snack")
        if (mealType !in validMealTypes) {
            throw IllegalArgumentException("Meal type is not valid")
        }
        currentFoods.forEach { tempFood ->
            if (tempFood.foodName.isBlank()) {
                throw IllegalArgumentException("Food name could not be blank")
            }
            if (tempFood.quantity <= 0f) {
                throw IllegalArgumentException("Quantity must be greater than 0")
            }
            if (tempFood.unit.isBlank()) {
                throw IllegalArgumentException("Please select food unit")
            }
            if (tempFood.calories < 0) {
                throw IllegalArgumentException("Please fill in the valid calories")
            }
        }

        val user = getLatestUserUseCase().firstOrNull()
            ?: throw IllegalStateException("Can't find user information")

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
