package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.DiaryDao
import com.yuika.healthtracker.data.local.entity.DiaryEntity
import com.yuika.healthtracker.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepositoryImpl @Inject constructor(
    private val diaryDao: DiaryDao
) : DiaryRepository
{
    override suspend fun insertEntry(entry: DiaryEntity): Long
    {
        return diaryDao.insertEntry(entry)
    }

    override suspend fun updateDiary(entry: DiaryEntity)
    {
        return diaryDao.updateDiary(entry)
    }

    override suspend fun deleteDiary(entry: DiaryEntity)
    {
        return diaryDao.deleteDiary(entry)
    }

    override fun getDiaryByDate(
        userId: Int,
        dateText: String
    ): Flow<DiaryEntity?>
    {
        return diaryDao.getDiaryByDate(userId, dateText)
    }

    override fun getWeightHistory(userId: Int): Flow<List<DiaryEntity>>
    {
        return diaryDao.getWeightHistory(userId)
    }
}