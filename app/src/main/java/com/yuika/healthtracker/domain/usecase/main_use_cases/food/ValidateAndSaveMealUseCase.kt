package com.yuika.healthtracker.domain.usecase.main_use_cases.food

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.ui.features.main_features.add_meal.TempFoodItem
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ValidateAndSaveMealUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveFoodEntryUseCase: SaveFoodEntryUseCase
) {
    suspend operator fun invoke(
        currentFoods: List<TempFoodItem>,
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
            val entity = FoodEntryEntity(
                userId = user.id,
                dateText = dateText,
                mealType = mealType,
                foodName = tempFood.foodName,
                quantity = tempFood.quantity,
                unit = tempFood.unit,
                calories = tempFood.calories,
                imagePath = null
            )
            saveFoodEntryUseCase(entity)
        }
    }
}
