package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.ActivityDao
import com.yuika.healthtracker.domain.model.Activity
import com.yuika.healthtracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityRepository
{
    override suspend fun insertActivity(activity: Activity): Long
    {
        return activityDao.insertActivity(activity.toEntity())
    }

    override suspend fun updateActivity(activity: Activity)
    {
        return activityDao.updateActivity(activity.toEntity())
    }

    override suspend fun deleteActivity(activity: Activity)
    {
        return activityDao.deleteActivity(activity.toEntity())
    }

    override fun getActivitiesByDate(
        userId: Int,
        dateText: String
    ): Flow<List<Activity>>
    {
        return activityDao.getActivitiesByDate(userId, dateText)
            .map { activities -> activities.map { it.toDomain() } }
    }

    override fun getTotalBurnedCaloriesByDate(
        userId: Int,
        dateText: String
    ): Flow<Int?>
    {
        return activityDao.getTotalBurnedCaloriesByDate(userId, dateText)
    }

    override fun getActivitiesByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Activity>>
    {
        return activityDao.getActivitiesByDateRange(userId, startDate, endDate)
            .map { activities -> activities.map { it.toDomain() } }
    }
}
