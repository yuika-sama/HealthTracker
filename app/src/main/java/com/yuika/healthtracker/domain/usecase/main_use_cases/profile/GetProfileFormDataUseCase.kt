package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

data class ProfileFormData(
    val id: Int,
    val name: String,
    val dateOfBirth: String,
    val gender: String,
    val weight: String,
    val height: String,
    val activityLevel: Float,
    val goal: String,
    val avatarPath: String?,
    val createdAt: Long
)

class GetProfileFormDataUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase
)
{
    suspend operator fun invoke(): ProfileFormData?
    {
        val user = getLatestUserUseCase().firstOrNull() ?: return null

        val levelFloat = when (user.activityLevel)
        {
            "sedentary" -> 1f
            "lightly_active" -> 2f
            "moderately_active" -> 3f
            "very_active" -> 4f
            "extra_active" -> 5f
            else -> 3f
        }

        return ProfileFormData(
            id = user.id,
            name = user.name,
            dateOfBirth = user.dateOfBirth,
            gender = user.gender,
            weight = user.weight.toString(),
            height = user.height.toString(),
            activityLevel = levelFloat,
            goal = user.goal,
            avatarPath = user.avatarPath,
            createdAt = user.createdAt
        )
    }
}
