package com.yuika.healthtracker.domain.usecase.main_use_cases.profile

import android.content.Context
import com.yuika.healthtracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yuika.healthtracker.domain.model.User
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.validateDateOfBirth
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.UpdateUserUseCase
import javax.inject.Inject

class ValidateAndSaveProfileUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val updateUserUseCase: UpdateUserUseCase
) {
    suspend operator fun invoke(
        id: Int,
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
            throw IllegalArgumentException(context.getString(R.string.error_enter_full_name))
        }

        val validDateOfBirth = validateDateOfBirth(context, dateOfBirth).first
        val weightDouble = weightStr.toDoubleOrNull()
        val heightDouble = heightStr.toDoubleOrNull()

        if (weightDouble == null || heightDouble == null) {
            throw IllegalArgumentException(context.getString(R.string.error_weight_height_number))
        }
        if (weightDouble !in 20.0..300.0) {
            throw IllegalArgumentException(context.getString(R.string.error_weight_between))
        }
        if (heightDouble !in 80.0..250.0) {
            throw IllegalArgumentException(context.getString(R.string.error_height_between))
        }
        if (gender.isBlank()) {
            throw IllegalArgumentException(context.getString(R.string.error_select_gender))
        }
        if (activityLevelFloat !in 1f..5f) {
            throw IllegalArgumentException(context.getString(R.string.error_activity_level_invalid))
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
            "lose_weight", "gain_weight", "maintain_weight" -> goalStr
            else -> "lose_weight"
        }

        val updatedUser = User(
            id = id,
            name = trimmedName,
            dateOfBirth = validDateOfBirth,
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
