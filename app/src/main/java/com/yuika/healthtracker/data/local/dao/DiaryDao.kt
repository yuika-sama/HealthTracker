package com.yuika.healthtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yuika.healthtracker.data.local.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DiaryEntity) = Long

    @Update
    suspend fun updateDiary(entry: DiaryEntity)

    @Delete
    suspend fun deleteDiary(entry: DiaryEntity)

    @Query("SELECT * FROM diaries WHERE dateText = :dateText LIMIT 1")
    fun getDiaryByDate(dateText: String): Flow<DiaryEntity?>

    @Query("SELECT * FROM diaries ORDER BY dateText ASC")
    fun getWeightHistory(): Flow<List<DiaryEntity>>
}