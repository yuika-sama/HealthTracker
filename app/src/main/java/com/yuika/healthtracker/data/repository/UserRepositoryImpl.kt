package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.UserDao
import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository
{
    override suspend fun insertUser(user: UserEntity): Long
    {
        return userDao.insertUser(user)
    }

    override suspend fun updateUser(user: UserEntity)
    {
        userDao.updateUser(user)
    }

    override suspend fun deleteUser(user: UserEntity)
    {
        userDao.deleteUser(user)
    }

    override suspend fun getUserById(userId: Int): UserEntity?
    {
        return userDao.getUserById(userId)
    }

    override suspend fun getUserByEmail(email: String): UserEntity?
    {
        return userDao.getUserByEmail(email)
    }

    override fun getUserByEmailFlow(email: String): Flow<UserEntity?>
    {
        return userDao.getUserByEmailFlow(email)
    }

    override fun getLatestUserFlow(): Flow<UserEntity?>
    {
        return userDao.getLatestUserFlow()
    }
}