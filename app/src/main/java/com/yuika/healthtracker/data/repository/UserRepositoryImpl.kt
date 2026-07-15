package com.yuika.healthtracker.data.repository

import com.yuika.healthtracker.data.local.dao.UserDao
import com.yuika.healthtracker.domain.model.User
import com.yuika.healthtracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository
{
    override suspend fun insertUser(user: User): Long
    {
        return userDao.insertUser(user.toEntity())
    }

    override suspend fun updateUser(user: User)
    {
        userDao.updateUser(user.toEntity())
    }

    override suspend fun deleteUser(user: User)
    {
        userDao.deleteUser(user.toEntity())
    }

    override suspend fun getUserById(userId: Int): User?
    {
        return userDao.getUserById(userId)?.toDomain()
    }

    override suspend fun getUserByEmail(email: String): User?
    {
        return userDao.getUserByEmail(email)?.toDomain()
    }

    override fun getUserByEmailFlow(email: String): Flow<User?>
    {
        return userDao.getUserByEmailFlow(email).map { it?.toDomain() }
    }

    override fun getLatestUserFlow(): Flow<User?>
    {
        return userDao.getLatestUserFlow().map { it?.toDomain() }
    }
}
