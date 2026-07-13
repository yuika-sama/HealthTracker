package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: UserEntity): Long {
        return userRepository.insertUser(user)
    }
}
