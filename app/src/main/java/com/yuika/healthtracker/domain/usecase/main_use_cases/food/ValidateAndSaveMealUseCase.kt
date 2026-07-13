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

        val user = getLatestUserUseCase().firstOrNull()
        if (user == null) {
            throw IllegalStateException("Can't find user information")
        }

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
