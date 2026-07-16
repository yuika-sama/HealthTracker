package com.yuika.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntryDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodEntry(entry: FoodEntryEntity): Long

    @Update
    suspend fun updateFoodEntry(entry: FoodEntryEntity)

    @Delete
    suspend fun deleteFoodEntry(entry: FoodEntryEntity)

    @Query("DELETE FROM food_entries WHERE id = :id")
    suspend fun deleteFoodEntryById(id: Int)

    @Query("SELECT * FROM food_entries WHERE userId = :userId AND dateText = :dateText AND mealType = :mealType ORDER BY timestamp ASC")
    fun getFoodEntriesByDateAndMealType(userId: Int, dateText: String, mealType: String): Flow<List<FoodEntryEntity>>

    @Query("SELECT * FROM food_entries WHERE userId = :userId AND dateText = :dateText ORDER BY timestamp ASC")
    fun getFoodEntriesByDate(userId: Int, dateText: String): Flow<List<FoodEntryEntity>>

    @Query("SELECT SUM(calories) FROM food_entries WHERE userId = :userId AND dateText = :dateText")
    fun getTotalCaloriesByDate(userId: Int, dateText: String): Flow<Int?>

    @Query("SELECT * FROM food_entries WHERE userId = :userId AND dateText BETWEEN :startDate AND :endDate ORDER BY dateText ASC")
    fun getFoodEntriesByDateRange(userId: Int, startDate: String, endDate: String): Flow<List<FoodEntryEntity>>
}