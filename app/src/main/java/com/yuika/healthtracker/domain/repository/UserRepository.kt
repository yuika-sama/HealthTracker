package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository
{
    suspend fun insertUser(user: UserEntity): Long

    suspend fun updateUser(user: UserEntity)

    suspend fun deleteUser(user: UserEntity)

    suspend fun getUserById(userId: Int): UserEntity?

    suspend fun getUserByEmail(email: String): UserEntity?

    fun getUserByEmailFlow(email: String): Flow<UserEntity?>

    fun getLatestUserFlow(): Flow<UserEntity?>
}