package com.yuika.healthtracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yuika.healthtracker.data.local.dao.ActivityCatalogDao
import com.yuika.healthtracker.data.local.dao.ActivityDao
import com.yuika.healthtracker.data.local.dao.DiaryDao
import com.yuika.healthtracker.data.local.dao.FoodCatalogDao
import com.yuika.healthtracker.data.local.dao.FoodEntryDao
import com.yuika.healthtracker.data.local.dao.UserDao
import com.yuika.healthtracker.data.local.entity.ActivityCatalogEntity
import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.data.local.entity.DiaryEntity
import com.yuika.healthtracker.data.local.entity.FoodCatalogEntity
import com.yuika.healthtracker.data.local.entity.FoodEntryEntity
import com.yuika.healthtracker.data.local.entity.UserEntity

@Database(
    entities = [
        DiaryEntity::class,
        UserEntity::class,
        FoodEntryEntity::class,
        ActivityEntity::class,
        FoodCatalogEntity::class,
        ActivityCatalogEntity::class
   ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun diaryDao(): DiaryDao
    abstract fun userDao(): UserDao
    abstract fun activityDao(): ActivityDao
    abstract fun foodEntryDao(): FoodEntryDao
    abstract fun foodCatalogDao(): FoodCatalogDao
    abstract fun activityCatalogDao(): ActivityCatalogDao
}
