package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.utils.NETWORK_DELAY
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class ResetPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
)
{
    suspend operator fun invoke(email: String?, newPasswordString: String){
        delay(NETWORK_DELAY.toLong().milliseconds)
        if (email == null) throw Exception("Email not provided")
        val user = userRepository.getUserByEmail(email)
            ?: throw Exception("User not found")
            
        userRepository.updateUser(user.copy(password = newPasswordString))
    }
}