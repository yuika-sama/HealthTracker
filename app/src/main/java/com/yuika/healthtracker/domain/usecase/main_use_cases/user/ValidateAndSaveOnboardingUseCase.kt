package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import com.yuika.healthtracker.data.local.entity.UserEntity
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ValidateAndSaveOnboardingUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) {
    suspend operator fun invoke(
        name: String,
        ageStr: String,
        gender: String,
        weightStr: String,
        heightStr: String
    ) {
        if (name.isBlank() || ageStr.isBlank() || weightStr.isBlank() || heightStr.isBlank()) {
            throw IllegalArgumentException("Please let me know your infomation.")
        }

        val weightValue = weightStr.toDoubleOrNull()
        val heightValue = heightStr.toDoubleOrNull()
        if (weightValue == null || heightValue == null) {
            throw IllegalArgumentException("Weight or height is not valid")
        }

        val user = getLatestUserUseCase().firstOrNull()
        if (user != null) {
            val updatedUser = user.copy(
                name = name,
                age = ageStr.toIntOrNull() ?: 25,
                gender = gender,
                weight = weightValue,
                height = heightValue
            )
            updateUserUseCase(updatedUser)
        } else {
            val newUser = UserEntity(
                email = "dummy@example.com",
                password = "dummy",
                name = name,
                age = ageStr.toIntOrNull() ?: 25,
                gender = gender,
                weight = weightValue,
                height = heightValue,
                activityLevel = "moderately_active",
                goal = "lose_weight",
                avatarPath = null
            )
            saveUserUseCase(newUser)
        }
    }
}
