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

    @Query("SELECT * FROM activities WHERE dateText = :dateText ORDER BY timestamp ASC")
    fun getActivitiesByDate(dateText: String): Flow<List<ActivityEntity>>

    @Query("SELECT SUM(kcalBurned) FROM activities WHERE dateText = :dateText")
    fun getTotalBurnedCaloriesByDate(dateText: String): Flow<Int?>
}