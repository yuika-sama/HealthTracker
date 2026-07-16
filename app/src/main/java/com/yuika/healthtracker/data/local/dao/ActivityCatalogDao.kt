package com.yuika.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yuika.healthtracker.data.local.entity.ActivityCatalogEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ActivityCatalogDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<ActivityCatalogEntity>)

    @Query("SELECT COUNT(*) FROM activity_catalog WHERE userId = :userId")
    suspend fun countByUser(userId: Int): Int

    @Query("SELECT * FROM activity_catalog WHERE userId = :userId ORDER BY name ASC")
    fun getAllByUser(userId: Int): Flow<List<ActivityCatalogEntity>>

    @Query("SELECT * FROM activity_catalog WHERE userId = :userId AND name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchByUser(userId: Int, query: String): Flow<List<ActivityCatalogEntity>>
}
