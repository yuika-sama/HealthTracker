package com.yuika.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yuika.healthtracker.data.local.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertEntry(entry: DiaryEntity)

    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<DiaryEntity>>
}