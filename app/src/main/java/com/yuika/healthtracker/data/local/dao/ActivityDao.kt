package com.yuika.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yuika.healthtracker.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity): Long

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)

    @Query("DELETE FROM activities WHERE id = :id")
    suspend fun deleteActivityById(id: Int)

    @Query("SELECT * FROM activities WHERE userId = :userId AND dateText = :dateText ORDER BY timestamp ASC")
    fun getActivitiesByDate(userId: Int, dateText: String): Flow<List<ActivityEntity>>

    @Query("SELECT SUM(kcalBurned) FROM activities WHERE userId = :userId AND dateText = :dateText")
    fun getTotalBurnedCaloriesByDate(userId: Int, dateText: String): Flow<Int?>

    @Query("SELECT * FROM activities WHERE userId = :userId AND dateText BETWEEN :startDate AND :endDate ORDER BY dateText ASC")
    fun getActivitiesByDateRange(userId: Int, startDate: String, endDate: String): Flow<List<ActivityEntity>>
}