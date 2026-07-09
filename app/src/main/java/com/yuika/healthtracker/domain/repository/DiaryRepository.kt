package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.data.local.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow

interface DiaryRepository
{
    suspend fun insertEntry(entry: DiaryEntity): Long

    suspend fun updateDiary(entry: DiaryEntity)

    suspend fun deleteDiary(entry: DiaryEntity)

    fun getDiaryByDate(userId: Int, dateText: String): Flow<DiaryEntity?>

    fun getWeightHistory(userId: Int): Flow<List<DiaryEntity>>
}