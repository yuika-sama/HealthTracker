package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.FoodCatalogDao
import com.yuika.healthtracker.domain.model.FoodCatalog
import com.yuika.healthtracker.domain.repository.FoodCatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodCatalogRepositoryImpl @Inject constructor(
    private val foodCatalogDao: FoodCatalogDao
) : FoodCatalogRepository
{
    override suspend fun insertAll(items: List<FoodCatalog>)
    {
        foodCatalogDao.insertAll(items.map { it.toEntity() })
    }

    override suspend fun isEmptyForUser(userId: Int): Boolean
    {
        return foodCatalogDao.countByUser(userId) == 0
    }

    override fun getAllByUser(userId: Int): Flow<List<FoodCatalog>>
    {
        return foodCatalogDao.getAllByUser(userId).map { items -> items.map { it.toDomain() } }
    }

    override fun searchByUser(userId: Int, query: String): Flow<List<FoodCatalog>>
    {
        return foodCatalogDao.searchByUser(userId, query).map { items -> items.map { it.toDomain() } }
    }
}
