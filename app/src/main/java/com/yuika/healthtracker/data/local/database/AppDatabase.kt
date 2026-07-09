package com.yuika.healthtracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuika.healthtracker.data.local.dao.ActivityDao
import com.yuika.healthtracker.data.local.dao.DiaryDao
import com.yuika.healthtracker.data.local.dao.FoodEntryDao
import com.yuika.healthtracker.data.local.dao.UserDao
import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.data.local.entity.DiaryEntity
import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.data.local.entity.UserEntity

@Database(
    entities = [
        DiaryEntity::class,
        UserEntity::class,
        FoodEntryEntity::class,
        ActivityEntity::class
   ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun diaryDao(): DiaryDao
    abstract fun userDao(): UserDao
    abstract fun activityDao(): ActivityDao
    abstract fun foodEntryDao(): FoodEntryDao
}