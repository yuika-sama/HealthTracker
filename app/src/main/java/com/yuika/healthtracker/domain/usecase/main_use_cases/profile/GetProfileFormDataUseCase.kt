package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import java.time.LocalDate
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

data class ProfileFormData(
    val id: Int,
    val email: String,
    val passwordHash: String,
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

        val goalStr = when (user.goal)
        {
            "lose_weight" -> "Lose weight"
            "gain_weight" -> "Weight gain"
            "maintain_weight" -> "Maintain weight"
            else -> "Lose weight"
        }

        return ProfileFormData(
            id = user.id,
            email = user.email,
            passwordHash = user.password,
            name = user.name,
            dateOfBirth = user.dateOfBirth ?: LocalDate.now().minusYears(user.age.toLong()).toString(),
            gender = user.gender,
            weight = user.weight.toString(),
            height = user.height.toString(),
            activityLevel = levelFloat,
            goal = goalStr,
            avatarPath = user.avatarPath,
            createdAt = user.createdAt
        )
    }
}
