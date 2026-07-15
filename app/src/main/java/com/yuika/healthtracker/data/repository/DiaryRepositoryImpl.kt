package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.DiaryDao
import com.yuika.healthtracker.domain.model.Diary
import com.yuika.healthtracker.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepositoryImpl @Inject constructor(
    private val diaryDao: DiaryDao
) : DiaryRepository
{
    override suspend fun insertEntry(entry: Diary): Long
    {
        return diaryDao.insertEntry(entry.toEntity())
    }

    override suspend fun updateDiary(entry: Diary)
    {
        return diaryDao.updateDiary(entry.toEntity())
    }

    override suspend fun deleteDiary(entry: Diary)
    {
        return diaryDao.deleteDiary(entry.toEntity())
    }

    override fun getDiaryByDate(
        userId: Int,
        dateText: String
    ): Flow<Diary?>
    {
        return diaryDao.getDiaryByDate(userId, dateText).map { it?.toDomain() }
    }

    override fun getWeightHistory(userId: Int): Flow<List<Diary>>
    {
        return diaryDao.getWeightHistory(userId).map { entries -> entries.map { it.toDomain() } }
    }
}
