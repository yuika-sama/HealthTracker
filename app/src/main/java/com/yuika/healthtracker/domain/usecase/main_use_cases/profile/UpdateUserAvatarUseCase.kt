package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.UpdateUserUseCase
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UpdateUserAvatarUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
)
{
    suspend operator fun invoke(avatarPath: String)
    {
        val path = avatarPath.trim()
        if (path.isBlank()) throw IllegalArgumentException("Please select avatar")

        val user = getLatestUserUseCase().firstOrNull()
            ?: throw IllegalArgumentException("Can't find user information")

        updateUserUseCase(user.copy(avatarPath = path))
    }
}
