package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository
{
    suspend fun insertActivity(activity: ActivityEntity): Long

    suspend fun updateActivity(activity: ActivityEntity)

    suspend fun deleteActivity(activity: ActivityEntity)

    fun getActivitiesByDate(userId: Int, dateText: String): Flow<List<ActivityEntity>>

    fun getTotalBurnedCaloriesByDate(userId: Int, dateText: String): Flow<Int?>

    fun getActivitiesByDateRange(userId: Int, startDate: String, endDate: String): Flow<List<ActivityEntity>>
}