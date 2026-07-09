package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.ActivityDao
import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityRepository
{
    override suspend fun insertActivity(activity: ActivityEntity): Long
    {
        return activityDao.insertActivity(activity)
    }

    override suspend fun updateActivity(activity: ActivityEntity)
    {
        return activityDao.updateActivity(activity)
    }

    override suspend fun deleteActivity(activity: ActivityEntity)
    {
        return activityDao.deleteActivity(activity)
    }

    override fun getActivitiesByDate(
        userId: Int,
        dateText: String
    ): Flow<List<ActivityEntity>>
    {
        return activityDao.getActivitiesByDate(userId, dateText)
    }

    override fun getTotalBurnedCaloriesByDate(
        userId: Int,
        dateText: String
    ): Flow<Int?>
    {
        return activityDao.getTotalBurnedCaloriesByDate(userId, dateText)
    }
}