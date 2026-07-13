package com.yuika.healthtracker.ui.features.main_features.update_profile

import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<UpdateProfileUiState, UpdateProfileIntent, UpdateProfileEffect>(
    initialState = UpdateProfileUiState()
) {

    override fun onIntent(intent: UpdateProfileIntent) {
        when (intent) {
            is UpdateProfileIntent.LoadProfile -> handleLoadProfile()
            is UpdateProfileIntent.UpdateName -> updateState { it.copy(name = intent.name) }
            is UpdateProfileIntent.UpdateAge -> updateState { it.copy(age = intent.age) }
            is UpdateProfileIntent.UpdateGender -> updateState { it.copy(gender = intent.gender) }
            is UpdateProfileIntent.UpdateWeight -> updateState { it.copy(weight = intent.weight) }
            is UpdateProfileIntent.UpdateHeight -> updateState { it.copy(height = intent.height) }
            is UpdateProfileIntent.UpdateActivityLevel -> updateState { it.copy(activityLevel = intent.level) }
            is UpdateProfileIntent.UpdateGoal -> updateState { it.copy(goal = intent.goal) }
            is UpdateProfileIntent.SaveProfile -> handleSaveProfile()
        }
    }

    private fun handleLoadProfile() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(UpdateProfileEffect.ShowError(throwable.message ?: "Error loading profile"))
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()
            if (user != null) {
                val levelFloat = when (user.activityLevel) {
                    "sedentary" -> 1f
                    "lightly_active" -> 2f
                    "moderately_active" -> 3f
                    "very_active" -> 4f
                    "extra_active" -> 5f
                    else -> 3f
                }
                
                val goalStr = when (user.goal) {
                    "lose_weight" -> "Lose weight"
                    "gain_weight" -> "Weight gain"
                    "maintain_weight" -> "Maintain weight"
                    else -> "Lose weight"
                }

                updateState {
                    it.copy(
                        id = user.id,
                        email = user.email,
                        passwordHash = user.password,
                        name = user.name,
                        age = user.age.toString(),
                        gender = user.gender,
                        weight = user.weight.toString(),
                        height = user.height.toString(),
                        activityLevel = levelFloat,
                        goal = goalStr,
                        avatarPath = user.avatarPath,
                        createdAt = user.createdAt,
                        isLoading = false
                    )
                }
            } else {
                updateState { it.copy(isLoading = false, errorMessage = "User not found") }
            }
        }
    }

    private fun handleSaveProfile() {
        val currentState = state.value
        
        if (currentState.name.isBlank() || currentState.age.isBlank() || 
            currentState.weight.isBlank() || currentState.height.isBlank()) {
            sendEffect(UpdateProfileEffect.ShowError("Please fill in your name"))
            return
        }

        val ageInt = currentState.age.toIntOrNull()
        val weightDouble = currentState.weight.toDoubleOrNull()
        val heightDouble = currentState.height.toDoubleOrNull()

        if (ageInt == null || weightDouble == null || heightDouble == null) {
            sendEffect(UpdateProfileEffect.ShowError("Age, weight and height must be a valid number"))
            return
        }

        updateState { it.copy(isSaving = true) }

        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isSaving = false) }
                sendEffect(UpdateProfileEffect.ShowError(throwable.message ?: "Error saving profile"))
            }
        ) {
            val levelStr = when (currentState.activityLevel.toInt()) {
                1 -> "sedentary"
                2 -> "lightly_active"
                3 -> "moderately_active"
                4 -> "very_active"
                5 -> "extra_active"
                else -> "moderately_active"
            }
            
            val goalDb = when (currentState.goal) {
                "Lose weight" -> "lose_weight"
                "Weight gain" -> "gain_weight"
                "Maintain weight" -> "maintain_weight"
                else -> "lose_weight"
            }

            val updatedUser = UserEntity(
                id = currentState.id,
                email = currentState.email,
                password = currentState.passwordHash,
                name = currentState.name,
                age = ageInt,
                gender = currentState.gender,
                weight = weightDouble,
                height = heightDouble,
                activityLevel = levelStr,
                goal = goalDb,
                avatarPath = currentState.avatarPath,
                createdAt = currentState.createdAt
            )

            userRepository.updateUser(updatedUser)
            
            updateState { it.copy(isSaving = false) }
            sendEffect(UpdateProfileEffect.ShowSuccess("Update successfully"))
            sendEffect(UpdateProfileEffect.NavigateBack)
        }
    }
}
