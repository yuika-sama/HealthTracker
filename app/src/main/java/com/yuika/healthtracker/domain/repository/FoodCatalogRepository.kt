package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.domain.model.FoodCatalog
import kotlinx.coroutines.flow.Flow

interface FoodCatalogRepository
{
    suspend fun insertAll(items: List<FoodCatalog>)

    suspend fun isEmptyForUser(userId: Int): Boolean

    fun getAllByUser(userId: Int): Flow<List<FoodCatalog>>

    fun searchByUser(userId: Int, query: String): Flow<List<FoodCatalog>>
}
