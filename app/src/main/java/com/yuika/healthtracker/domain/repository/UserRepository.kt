package com.yuika.healthtracker.domain.repository

import com.yuika.healthtracker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository
{
    suspend fun insertUser(user: User): Long

    suspend fun updateUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun getUserById(userId: Int): User?

    fun getLatestUserFlow(): Flow<User?>
}
