package com.yuika.healthtracker.ui.features.main_features.update_profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.GetProfileFormDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.ValidateAndSaveProfileUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val getProfileFormDataUseCase: GetProfileFormDataUseCase,
    private val validateAndSaveProfileUseCase: ValidateAndSaveProfileUseCase
) : BaseViewModel<UpdateProfileUiState, UpdateProfileIntent, UpdateProfileEffect>(
    initialState = UpdateProfileUiState()
)
{

    private var currentPasswordHash: String = ""

    override fun onIntent(intent: UpdateProfileIntent)
    {
        when (intent)
        {
            is UpdateProfileIntent.LoadProfile -> handleLoadProfile()
            is UpdateProfileIntent.UpdateName -> updateState { it.copy(name = intent.name) }
            is UpdateProfileIntent.UpdateDateOfBirth -> updateState { it.copy(dateOfBirth = intent.dateOfBirth) }
            is UpdateProfileIntent.UpdateGender -> updateState { it.copy(gender = intent.gender) }
            is UpdateProfileIntent.UpdateWeight -> updateState { it.copy(weight = intent.weight) }
            is UpdateProfileIntent.UpdateHeight -> updateState { it.copy(height = intent.height) }
            is UpdateProfileIntent.UpdateActivityLevel -> updateState {
                it.copy(
                    activityLevel = intent.level.roundToInt().coerceIn(1, 5).toFloat()
                )
            }

            is UpdateProfileIntent.UpdateGoal -> updateState { it.copy(goal = intent.goal) }
            is UpdateProfileIntent.SaveProfile -> handleSaveProfile()
        }
    }

    private fun handleLoadProfile()
    {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Error loading profile"
                updateState { it.copy(isLoading = false, errorMessage = message) }
            }
        ) {
            val formData = getProfileFormDataUseCase()
            if (formData != null)
            {
                currentPasswordHash = formData.passwordHash
                delay(NETWORK_DELAY.toLong())
                updateState {
                    it.copy(
                        id = formData.id,
                        email = formData.email,
                        name = formData.name,
                        dateOfBirth = formData.dateOfBirth,
                        gender = formData.gender,
                        weight = formData.weight,
                        height = formData.height,
                        activityLevel = formData.activityLevel,
                        goal = formData.goal,
                        avatarPath = formData.avatarPath,
                        createdAt = formData.createdAt,
                        isLoading = false,
                        isSuccess = true,
                        errorMessage = null
                    )
                }
            }
            else
            {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = "User not found",
                        isSuccess = false
                    )
                }
            }
        }
    }

    private fun handleSaveProfile()
    {
        val currentState = state.value

        updateState { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Error saving profile"
                updateState { it.copy(isSaving = false, errorMessage = message) }
            }
        ) {
            validateAndSaveProfileUseCase(
                id = currentState.id,
                email = currentState.email,
                passwordHash = currentPasswordHash,
                name = currentState.name,
                dateOfBirth = currentState.dateOfBirth,
                gender = currentState.gender,
                weightStr = currentState.weight,
                heightStr = currentState.height,
                activityLevelFloat = currentState.activityLevel,
                goalStr = currentState.goal,
                avatarPath = currentState.avatarPath,
                createdAt = currentState.createdAt
            )

            updateState { it.copy(isSaving = false, isSuccess = true) }
            sendEffect(UpdateProfileEffect.ShowSuccess("Update successfully"))
            sendEffect(UpdateProfileEffect.NavigateBack)
        }
    }
}
