package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.domain.model.FoodEntry
import kotlinx.coroutines.flow.Flow

interface FoodEntryRepository
{
    suspend fun insertFoodEntry(entry: FoodEntry): Long

    suspend fun updateFoodEntry(entry: FoodEntry)

    suspend fun deleteFoodEntry(entry: FoodEntry)

    suspend fun deleteFoodEntryById(id: Int)

    fun getFoodEntriesByDateAndMealType(userId: Int, dateText: String, mealType: String): Flow<List<FoodEntry>>

    fun getFoodEntriesByDate(userId: Int, dateText: String): Flow<List<FoodEntry>>

    fun getTotalCaloriesByDate(userId: Int, dateText: String): Flow<Int?>

    fun getFoodEntriesByDateRange(userId: Int, startDate: String, endDate: String) : Flow<List<FoodEntry>>
}
