package com.yuika.healthtracker.domain.usecase.auth_use_cases

import com.yuika.healthtracker.domain.repository.UserRepository
import javax.inject.Inject

class CheckEmailExistsUseCase @Inject constructor(
    private val userRepository: UserRepository
)
{
    suspend operator fun invoke(email: String): Boolean {
        val user = userRepository.getUserByEmail(email)
        return user != null
    }
}