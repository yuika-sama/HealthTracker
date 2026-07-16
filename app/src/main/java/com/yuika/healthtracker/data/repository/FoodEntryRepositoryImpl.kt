package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.FoodEntryDao
import com.yuika.healthtracker.domain.model.FoodEntry
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodEntryRepositoryImpl @Inject constructor(
    private val foodEntryDao: FoodEntryDao
) : FoodEntryRepository
{
    override suspend fun insertFoodEntry(entry: FoodEntry): Long
    {
        return foodEntryDao.insertFoodEntry(entry.toEntity())
    }

    override suspend fun updateFoodEntry(entry: FoodEntry)
    {
        foodEntryDao.updateFoodEntry(entry.toEntity())
    }

    override suspend fun deleteFoodEntry(entry: FoodEntry)
    {
        foodEntryDao.deleteFoodEntry(entry.toEntity())
    }

    override suspend fun deleteFoodEntryById(id: Int)
    {
        foodEntryDao.deleteFoodEntryById(id)
    }

    override fun getFoodEntriesByDateAndMealType(
        userId: Int,
        dateText: String,
        mealType: String
    ): Flow<List<FoodEntry>>
    {
        return foodEntryDao.getFoodEntriesByDateAndMealType(userId, dateText, mealType)
            .map { entries -> entries.map { it.toDomain() } }
    }

    override fun getFoodEntriesByDate(
        userId: Int,
        dateText: String
    ): Flow<List<FoodEntry>>
    {
        return foodEntryDao.getFoodEntriesByDate(userId, dateText)
            .map { entries -> entries.map { it.toDomain() } }
    }

    override fun getTotalCaloriesByDate(
        userId: Int,
        dateText: String
    ): Flow<Int?>
    {
        return foodEntryDao.getTotalCaloriesByDate(userId, dateText)
    }

    override fun getFoodEntriesByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<FoodEntry>>
    {
        return foodEntryDao.getFoodEntriesByDateRange(userId, startDate, endDate)
            .map { entries -> entries.map { it.toDomain() } }
    }
}
