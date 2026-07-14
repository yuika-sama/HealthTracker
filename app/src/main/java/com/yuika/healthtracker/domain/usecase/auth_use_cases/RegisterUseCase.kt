package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.utils.MOCK_ERROR_LOGIN_EMAIL
import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
)
{
    suspend operator fun invoke(fullName: String, email: String, age: String, password: String) {
        delay(NETWORK_DELAY.toLong().milliseconds)
        if (email == MOCK_ERROR_LOGIN_EMAIL) {
            throw Exception("Error connect to server")
        }
        val existingUser = userRepository.getUserByEmail(email)
        if (existingUser != null) {
            throw Exception("Email already exists")
        }

        userRepository.insertUser(
            UserEntity(
                email = email,
                password = password,
                name = fullName,
                age = age.toIntOrNull() ?: 18,
                gender = "Other",
                height = 0.0,
                weight = 0.0,
                activityLevel = "None",
                goal = "None",
                avatarPath = null
            )
        )
    }
}