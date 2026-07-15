package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.domain.model.User
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.utils.MOCK_ERROR_LOGIN_EMAIL
import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
)
{
    suspend operator fun invoke(email: String, password: String): User {
        delay(NETWORK_DELAY.toLong().milliseconds)

        if (email == MOCK_ERROR_LOGIN_EMAIL){
            throw Exception("Error connect to server")
        }

        val user = userRepository.getUserByEmail(email)
        if (user != null && user.password == password){
            return user
        } else {
            throw Exception("Email or password is wrong")
        }
    }
}
