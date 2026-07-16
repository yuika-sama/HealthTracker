package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import com.yuika.healthtracker.domain.model.User
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.validateDateOfBirth
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.UpdateUserUseCase
import javax.inject.Inject

class ValidateAndSaveProfileUseCase @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase
) {
    suspend operator fun invoke(
        id: Int,
        email: String,
        passwordHash: String,
        name: String,
        dateOfBirth: String,
        gender: String,
        weightStr: String,
        heightStr: String,
        activityLevelFloat: Float,
        goalStr: String,
        avatarPath: String?,
        createdAt: Long
    ) {
        val trimmedName = name.trim()
        if (trimmedName.isBlank() || dateOfBirth.isBlank() || weightStr.isBlank() || heightStr.isBlank()) {
            throw IllegalArgumentException("Please fill in your name")
        }

        val (validDateOfBirth, ageInt) = validateDateOfBirth(dateOfBirth)
        val weightDouble = weightStr.toDoubleOrNull()
        val heightDouble = heightStr.toDoubleOrNull()

        if (weightDouble == null || heightDouble == null) {
            throw IllegalArgumentException("Weight and height must be a valid number")
        }
        if (weightDouble !in 20.0..300.0) {
            throw IllegalArgumentException("Weight must be between 20 and 300 kg")
        }
        if (heightDouble !in 80.0..250.0) {
            throw IllegalArgumentException("Height must be between 80 and 250 cm")
        }
        if (gender.isBlank()) {
            throw IllegalArgumentException("Please select your gender")
        }
        if (activityLevelFloat !in 1f..5f) {
            throw IllegalArgumentException("Activity level is not valid")
        }

        val levelStr = when (activityLevelFloat.toInt()) {
            1 -> "sedentary"
            2 -> "lightly_active"
            3 -> "moderately_active"
            4 -> "very_active"
            5 -> "extra_active"
            else -> "moderately_active"
        }
        
        val goalDb = when (goalStr) {
            "Lose weight" -> "lose_weight"
            "Weight gain" -> "gain_weight"
            "Maintain weight" -> "maintain_weight"
            else -> "lose_weight"
        }

        val updatedUser = User(
            id = id,
            email = email,
            password = passwordHash,
            name = trimmedName,
            dateOfBirth = validDateOfBirth,
            age = ageInt,
            gender = gender,
            weight = weightDouble,
            height = heightDouble,
            activityLevel = levelStr,
            goal = goalDb,
            avatarPath = avatarPath,
            createdAt = createdAt
        )

        updateUserUseCase(updatedUser)
    }
}
