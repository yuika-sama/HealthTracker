package com.yuika.healthtracker.service.di

import android.content.Context
import androidx.room.Room
import com.yuika.healthtracker.data.local.dao.ActivityDao
import com.yuika.healthtracker.data.local.dao.DiaryDao
import com.yuika.healthtracker.data.local.dao.FoodEntryDao
import com.yuika.healthtracker.data.local.dao.UserDao
import com.yuika.healthtracker.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
{
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideDiaryDao(database: AppDatabase): DiaryDao{
        return database.diaryDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao{
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideActivityDao(database: AppDatabase): ActivityDao{
        return database.activityDao()
    }

    @Provides
    @Singleton
    fun provideFoodEntryDao(database: AppDatabase): FoodEntryDao{
        return database.foodEntryDao()
    }
}