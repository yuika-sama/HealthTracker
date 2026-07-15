package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import com.yuika.healthtracker.domain.model.User
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
        val trimmedName = name.trim()
        if (trimmedName.isBlank() || ageStr.isBlank() || weightStr.isBlank() || heightStr.isBlank()) {
            throw IllegalArgumentException("Please let me know your infomation.")
        }

        val ageValue = ageStr.toIntOrNull()
        val weightValue = weightStr.toDoubleOrNull()
        val heightValue = heightStr.toDoubleOrNull()
        if (ageValue == null || ageValue !in 10..120) {
            throw IllegalArgumentException("Age must be between 10 and 120")
        }
        if (weightValue == null || weightValue !in 20.0..300.0) {
            throw IllegalArgumentException("Weight must be between 20 and 300 kg")
        }
        if (heightValue == null || heightValue !in 80.0..250.0) {
            throw IllegalArgumentException("Height must be between 80 and 250 cm")
        }
        if (gender.isBlank()) {
            throw IllegalArgumentException("Please select your gender")
        }

        val user = getLatestUserUseCase().firstOrNull()
        if (user != null) {
            val updatedUser = user.copy(
                name = trimmedName,
                age = ageValue,
                gender = gender,
                weight = weightValue,
                height = heightValue
            )
            updateUserUseCase(updatedUser)
        } else {
            val newUser = User(
                email = "dummy@example.com",
                password = "dummy",
                name = trimmedName,
                age = ageValue,
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
