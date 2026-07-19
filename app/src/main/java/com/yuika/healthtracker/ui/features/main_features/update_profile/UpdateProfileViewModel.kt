package com.yuika.healthtracker.ui.features.main_features.update_profile

import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.GetProfileFormDataUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.profile.ValidateAndSaveProfileUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.validateDateOfBirth
import com.yuika.healthtracker.service.widget.WidgetService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val getProfileFormDataUseCase: GetProfileFormDataUseCase,
    private val validateAndSaveProfileUseCase: ValidateAndSaveProfileUseCase,
    private val widgetService: WidgetService
) : BaseViewModel<UpdateProfileUiState, UpdateProfileIntent, UpdateProfileEffect>(
    initialState = UpdateProfileUiState()
)
{
    override fun onIntent(intent: UpdateProfileIntent)
    {
        when (intent)
        {
            is UpdateProfileIntent.LoadProfile -> handleLoadProfile()
            is UpdateProfileIntent.UpdateName -> updateState { it.copy(name = intent.name, nameError = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateDateOfBirth -> updateState {
                it.copy(dateOfBirth = intent.dateOfBirth, dateOfBirthError = null, isSuccess = false)
            }
            is UpdateProfileIntent.UpdateGender -> updateState { it.copy(gender = intent.gender, genderError = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateWeight -> updateState { it.copy(weight = intent.weight, weightError = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateHeight -> updateState { it.copy(height = intent.height, heightError = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateActivityLevel -> updateState {
                it.copy(
                    activityLevel = intent.level.roundToInt().coerceIn(1, 5).toFloat(),
                    activityLevelError = null,
                    isSuccess = false
                )
            }

            is UpdateProfileIntent.UpdateGoal -> updateState { it.copy(goal = intent.goal, goalError = null, isSuccess = false) }
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
                delay(NETWORK_DELAY.toLong())
                updateState {
                    it.copy(
                        id = formData.id,
                        name = formData.name,
                        dateOfBirth = formData.dateOfBirth,
                        gender = formData.gender,
                        weight = formData.weight,
                        height = formData.height,
                        activityLevel = formData.activityLevel,
                        goal = formData.goal,
                        avatarPath = formData.avatarPath,
                        createdAt = formData.createdAt,
                        nameError = null,
                        dateOfBirthError = null,
                        genderError = null,
                        weightError = null,
                        heightError = null,
                        activityLevelError = null,
                        goalError = null,
                        isLoading = false,
                        isSuccess = false,
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
        val validGoals = listOf("Lose weight", "Maintain weight", "Weight gain")
        val weightValue = currentState.weight.toDoubleOrNull()
        val heightValue = currentState.height.toDoubleOrNull()
        val nameError = if (currentState.name.trim().isBlank()) "Please enter your full name" else null
        val dateOfBirthError = when
        {
            currentState.dateOfBirth.isBlank() -> "Please select your date of birth"
            else -> runCatching { validateDateOfBirth(currentState.dateOfBirth) }.exceptionOrNull()?.message
        }
        val genderError = if (currentState.gender.isBlank()) "Please select your gender" else null
        val weightError = when
        {
            currentState.weight.isBlank() -> "Please enter your weight"
            weightValue == null -> "Weight must be a number"
            weightValue !in 20.0..300.0 -> "Weight must be between 20 and 300 kg"
            else -> null
        }
        val heightError = when
        {
            currentState.height.isBlank() -> "Please enter your height"
            heightValue == null -> "Height must be a number"
            heightValue !in 80.0..250.0 -> "Height must be between 80 and 250 cm"
            else -> null
        }
        val activityLevelError =
            if (currentState.activityLevel !in 1f..5f) "Activity level is not valid" else null
        val goalError = if (currentState.goal !in validGoals) "Please select your goal" else null

        if (listOf(
                nameError,
                dateOfBirthError,
                genderError,
                weightError,
                heightError,
                activityLevelError,
                goalError
            ).any { it != null }
        ) {
            updateState {
                it.copy(
                    isSaving = false,
                    errorMessage = null,
                    isSuccess = false,
                    nameError = nameError,
                    dateOfBirthError = dateOfBirthError,
                    genderError = genderError,
                    weightError = weightError,
                    heightError = heightError,
                    activityLevelError = activityLevelError,
                    goalError = goalError
                )
            }
            return
        }

        updateState { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Error saving profile"
                updateState { it.copy(isSaving = false, errorMessage = message) }
            }
        ) {
            validateAndSaveProfileUseCase(
                id = currentState.id,
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

            widgetService.refresh()

            updateState { it.copy(isSaving = false, isSuccess = true) }
            sendEffect(UpdateProfileEffect.ShowSuccess("Update successfully"))
            sendEffect(UpdateProfileEffect.NavigateBack)
        }
    }
}
