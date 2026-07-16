package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.domain.model.ActivityCatalog
import kotlinx.coroutines.flow.Flow

interface ActivityCatalogRepository
{
    suspend fun insertAll(items: List<ActivityCatalog>)

    suspend fun isEmptyForUser(userId: Int): Boolean

    fun getAllByUser(userId: Int): Flow<List<ActivityCatalog>>

    fun searchByUser(userId: Int, query: String): Flow<List<ActivityCatalog>>
}
