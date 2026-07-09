package com.yuika.healthtracker.service.di

import com.yuika.healthtracker.data.repository.ActivityRepositoryImpl
import com.yuika.healthtracker.data.repository.DiaryRepositoryImpl
import com.yuika.healthtracker.data.repository.FoodEntryRepositoryImpl
import com.yuika.healthtracker.data.repository.UserRepositoryImpl
import com.yuika.healthtracker.domain.repository.ActivityRepository
import com.yuika.healthtracker.domain.repository.DiaryRepository
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFoodEntryRepository(
        foodEntryRepositoryImpl: FoodEntryRepositoryImpl
    ): FoodEntryRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(
        activityRepositoryImpl: ActivityRepositoryImpl
    ): ActivityRepository

    @Binds
    @Singleton
    abstract fun bindDiaryRepository(
        diaryRepositoryImpl: DiaryRepositoryImpl
    ): DiaryRepository
}