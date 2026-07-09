package com.yuika.healthtracker.service.di

import com.yuika.healthtracker.data.repository.FoodEntryRepositoryImpl
import com.yuika.healthtracker.data.repository.UserRepositoryImpl
import com.yuika.healthtracker.domain.repository.FoodEntryRepository
import com.yuika.healthtracker.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
abstract class RepositoryModule
{
    @Binds
    @Singleton
    abstract fun bindFoodEntryRepository(
        foodEntryRepositoryImpl: FoodEntryRepositoryImpl
    ): FoodEntryRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository
}