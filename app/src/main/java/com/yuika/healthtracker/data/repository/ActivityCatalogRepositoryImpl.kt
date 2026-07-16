package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.ActivityCatalogDao
import com.yuika.healthtracker.domain.model.ActivityCatalog
import com.yuika.healthtracker.domain.repository.ActivityCatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityCatalogRepositoryImpl @Inject constructor(
    private val activityCatalogDao: ActivityCatalogDao
) : ActivityCatalogRepository
{
    override suspend fun insertAll(items: List<ActivityCatalog>)
    {
        activityCatalogDao.insertAll(items.map { it.toEntity() })
    }

    override suspend fun isEmptyForUser(userId: Int): Boolean
    {
        return activityCatalogDao.countByUser(userId) == 0
    }

    override fun getAllByUser(userId: Int): Flow<List<ActivityCatalog>>
    {
        return activityCatalogDao.getAllByUser(userId).map { items -> items.map { it.toDomain() } }
    }

    override fun searchByUser(userId: Int, query: String): Flow<List<ActivityCatalog>>
    {
        return activityCatalogDao.searchByUser(userId, query).map { items -> items.map { it.toDomain() } }
    }
}
