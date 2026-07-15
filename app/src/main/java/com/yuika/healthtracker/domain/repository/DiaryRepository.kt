package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.domain.model.Diary
import kotlinx.coroutines.flow.Flow

interface DiaryRepository
{
    suspend fun insertEntry(entry: Diary): Long

    suspend fun updateDiary(entry: Diary)

    suspend fun deleteDiary(entry: Diary)

    fun getDiaryByDate(userId: Int, dateText: String): Flow<Diary?>

    fun getWeightHistory(userId: Int): Flow<List<Diary>>
}
