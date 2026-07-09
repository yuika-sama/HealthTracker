package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.FoodEntryDao
import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodEntryRepositoryImpl @Inject constructor(
    private val foodEntryDao: FoodEntryDao
) : FoodEntryRepository
{
    override suspend fun insertFoodEntry(entry: FoodEntryEntity): Long
    {
        return foodEntryDao.insertFoodEntry(entry)
    }

    override suspend fun updateFoodEntry(entry: FoodEntryEntity)
    {
        foodEntryDao.updateFoodEntry(entry)
    }

    override suspend fun deleteFoodEntry(entry: FoodEntryEntity)
    {
        foodEntryDao.deleteFoodEntry(entry)
    }

    override fun getFoodEntriesByDateAndMealType(
        userId: Int,
        dateText: String,
        mealType: String
    ): Flow<List<FoodEntryEntity>>
    {
        return foodEntryDao.getFoodEntriesByDateAndMealType(userId, dateText, mealType)
    }

    override fun getFoodEntriesByDate(
        userId: Int,
        dateText: String
    ): Flow<List<FoodEntryEntity>>
    {
        return foodEntryDao.getFoodEntriesByDate(userId, dateText)
    }

    override fun getTotalCaloriesByDate(
        userId: Int,
        dateText: String
    ): Flow<Int?>
    {
        return foodEntryDao.getTotalCaloriesByDate(userId, dateText)
    }
}