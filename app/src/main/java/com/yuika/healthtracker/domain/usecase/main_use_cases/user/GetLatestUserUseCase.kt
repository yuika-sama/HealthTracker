package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserEntity?> {
        return userRepository.getLatestUserFlow()
    }
}
