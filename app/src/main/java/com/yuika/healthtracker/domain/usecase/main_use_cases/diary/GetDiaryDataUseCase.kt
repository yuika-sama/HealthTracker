package com.yuika.healthtracker.domain.usecase.main_use_cases.diary

import com.yuika.healthtracker.domain.model.FoodEntry
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.GetFoodEntriesByDateUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class DiaryFoodItem(
    val id: Int,
    val mealType: String,
    val name: String,
    val quantityText: String,
    val unit: String,
    val kcal: Int
)

data class DiaryData(
    val totalKcalGoal: Int,
    val totalKcalConsumed: Int,
    val proteinGrams: Int,
    val fatGrams: Int,
    val carbsGrams: Int,
    val breakfastFoods: List<DiaryFoodItem>,
    val lunchFoods: List<DiaryFoodItem>,
    val dinnerFoods: List<DiaryFoodItem>,
    val snackFoods: List<DiaryFoodItem>,
    val breakfastTotalKcal: Int,
    val lunchTotalKcal: Int,
    val dinnerTotalKcal: Int,
    val snackTotalKcal: Int
)

class GetDiaryDataUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val getFoodEntriesByDateUseCase: GetFoodEntriesByDateUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(dateText: String): Flow<DiaryData?> {
        return getLatestUserUseCase().flatMapLatest { user ->
            if (user == null) {
                flowOf<DiaryData?>(null)
            } else {
                getFoodEntriesByDateUseCase(user.id, dateText).map { allFoods: List<FoodEntry> ->
                    val stats = calculateUserStatsUseCase(user)

                    val breakfastEntries = allFoods.filter { it.mealType.equals("Breakfast", ignoreCase = true) }
                    val lunchEntries = allFoods.filter { it.mealType.equals("Lunch", ignoreCase = true) }
                    val dinnerEntries = allFoods.filter { it.mealType.equals("Dinner", ignoreCase = true) }
                    val snackEntries = allFoods.filter { it.mealType.equals("Snack", ignoreCase = true) }

                    val breakfastKcal = breakfastEntries.sumOf { it.calories }
                    val lunchKcal = lunchEntries.sumOf { it.calories }
                    val dinnerKcal = dinnerEntries.sumOf { it.calories }
                    val snackKcal = snackEntries.sumOf { it.calories }

                    val totalConsumed = breakfastKcal + lunchKcal + dinnerKcal + snackKcal

                    val proteinGrams = ((totalConsumed * 0.30) / 4).toInt()
                    val fatGrams = ((totalConsumed * 0.25) / 9).toInt()
                    val carbsGrams = ((totalConsumed * 0.45) / 4).toInt()

                    fun mapToItems(entries: List<FoodEntry>): List<DiaryFoodItem> = entries.map {
                        DiaryFoodItem(
                            id = it.id,
                            mealType = it.mealType,
                            name = it.foodName,
                            quantityText = it.quantity.toString(),
                            unit = it.unit,
                            kcal = it.calories
                        )
                    }

                    DiaryData(
                        totalKcalGoal = stats.goalKcal,
                        totalKcalConsumed = totalConsumed,
                        proteinGrams = proteinGrams,
                        fatGrams = fatGrams,
                        carbsGrams = carbsGrams,
                        breakfastFoods = mapToItems(breakfastEntries),
                        lunchFoods = mapToItems(lunchEntries),
                        dinnerFoods = mapToItems(dinnerEntries),
                        snackFoods = mapToItems(snackEntries),
                        breakfastTotalKcal = breakfastKcal,
                        lunchTotalKcal = lunchKcal,
                        dinnerTotalKcal = dinnerKcal,
                        snackTotalKcal = snackKcal
                    )
                }
            }
        }
    }
}
