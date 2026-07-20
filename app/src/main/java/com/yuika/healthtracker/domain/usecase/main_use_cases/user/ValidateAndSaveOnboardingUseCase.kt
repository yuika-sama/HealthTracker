package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import android.content.Context
import com.yuika.healthtracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yuika.healthtracker.domain.model.User
import com.yuika.healthtracker.domain.usecase.main_use_cases.catalog.EnsureUserCatalogSeedUseCase
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ValidateAndSaveOnboardingUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val ensureUserCatalogSeedUseCase: EnsureUserCatalogSeedUseCase
) {
    suspend operator fun invoke(
        name: String,
        dateOfBirth: String,
        gender: String,
        weightStr: String,
        heightStr: String
    ) {
        val trimmedName = name.trim()
        if (trimmedName.isBlank() || dateOfBirth.isBlank() || weightStr.isBlank() || heightStr.isBlank()) {
            throw IllegalArgumentException(context.getString(R.string.error_let_me_know_information))
        }

        val validDateOfBirth = validateDateOfBirth(context, dateOfBirth).first
        val weightValue = weightStr.toDoubleOrNull()
        val heightValue = heightStr.toDoubleOrNull()
        if (weightValue == null || weightValue !in 20.0..300.0) {
            throw IllegalArgumentException(context.getString(R.string.error_weight_between))
        }
        if (heightValue == null || heightValue !in 80.0..250.0) {
            throw IllegalArgumentException(context.getString(R.string.error_height_between))
        }
        if (gender.isBlank()) {
            throw IllegalArgumentException(context.getString(R.string.error_select_gender))
        }

        val user = getLatestUserUseCase().firstOrNull()
        if (user != null) {
            val updatedUser = user.copy(
                name = trimmedName,
                dateOfBirth = validDateOfBirth,
                gender = gender,
                weight = weightValue,
                height = heightValue
            )
            updateUserUseCase(updatedUser)
            ensureUserCatalogSeedUseCase(updatedUser.id)
        } else {
            val newUser = User(
                name = trimmedName,
                dateOfBirth = validDateOfBirth,
                gender = gender,
                weight = weightValue,
                height = heightValue,
                activityLevel = "moderately_active",
                goal = "lose_weight",
                avatarPath = null
            )
            val userId = saveUserUseCase(newUser).toInt()
            ensureUserCatalogSeedUseCase(userId)
        }
    }
}
