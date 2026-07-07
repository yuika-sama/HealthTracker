package com.yuika.healthtracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuika.healthtracker.data.local.dao.DiaryDao
import com.yuika.healthtracker.data.local.entity.DiaryEntity

@Database(
    entities = [DiaryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun diaryDao(): DiaryDao
}