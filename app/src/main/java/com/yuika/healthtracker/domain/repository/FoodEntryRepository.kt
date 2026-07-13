package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import kotlinx.coroutines.flow.Flow

interface FoodEntryRepository
{
    suspend fun insertFoodEntry(entry: FoodEntryEntity): Long

    suspend fun updateFoodEntry(entry: FoodEntryEntity)

    suspend fun deleteFoodEntry(entry: FoodEntryEntity)

    fun getFoodEntriesByDateAndMealType(userId: Int, dateText: String, mealType: String): Flow<List<FoodEntryEntity>>

    fun getFoodEntriesByDate(userId: Int, dateText: String): Flow<List<FoodEntryEntity>>

    fun getTotalCaloriesByDate(userId: Int, dateText: String): Flow<Int?>

    fun getFoodEntriesByDateRange(userId: Int, startDate: String, endDate: String) : Flow<List<FoodEntryEntity>>
}