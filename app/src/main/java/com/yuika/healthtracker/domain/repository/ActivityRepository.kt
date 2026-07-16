package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository
{
    suspend fun insertActivity(activity: Activity): Long

    suspend fun updateActivity(activity: Activity)

    suspend fun deleteActivity(activity: Activity)

    suspend fun deleteActivityById(id: Int)

    fun getActivitiesByDate(userId: Int, dateText: String): Flow<List<Activity>>

    fun getTotalBurnedCaloriesByDate(userId: Int, dateText: String): Flow<Int?>

    fun getActivitiesByDateRange(userId: Int, startDate: String, endDate: String): Flow<List<Activity>>
}
