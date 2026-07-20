package com.yuika.healthtracker.ui.features.main_features.update_profile

import androidx.annotation.StringRes
import com.yuika.healthtracker.R
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
            is UpdateProfileIntent.UpdateName -> updateState { it.copy(name = intent.name, nameErrorRes = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateDateOfBirth -> updateState {
                it.copy(dateOfBirth = intent.dateOfBirth, dateOfBirthErrorRes = null, isSuccess = false)
            }
            is UpdateProfileIntent.UpdateGender -> updateState { it.copy(gender = intent.gender, genderErrorRes = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateWeight -> updateState { it.copy(weight = intent.weight, weightErrorRes = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateHeight -> updateState { it.copy(height = intent.height, heightErrorRes = null, isSuccess = false) }
            is UpdateProfileIntent.UpdateActivityLevel -> updateState {
                it.copy(
                    activityLevel = intent.level.roundToInt().coerceIn(1, 5).toFloat(),
                    activityLevelErrorRes = null,
                    isSuccess = false
                )
            }

            is UpdateProfileIntent.UpdateGoal -> updateState { it.copy(goal = intent.goal, goalErrorRes = null, isSuccess = false) }
            is UpdateProfileIntent.SaveProfile -> handleSaveProfile()
            is UpdateProfileIntent.UpdateAvatar -> updateState { it.copy(avatarPath = intent.avatarPath, isSuccess = false) }
        }
    }

    private fun handleLoadProfile()
    {
        updateState { it.copy(isLoading = true, errorMessageRes = null, isSuccess = false) }
        launchSafe(
            onError = {
                updateState { it.copy(isLoading = false, errorMessageRes = R.string.error_profile_loading) }
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
                        nameErrorRes = null,
                        dateOfBirthErrorRes = null,
                        genderErrorRes = null,
                        weightErrorRes = null,
                        heightErrorRes = null,
                        activityLevelErrorRes = null,
                        goalErrorRes = null,
                        isLoading = false,
                        isSuccess = false,
                        errorMessageRes = null
                    )
                }
            }
            else
            {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = R.string.error_cannot_find_user_info,
                        isSuccess = false
                    )
                }
            }
        }
    }

    private fun handleSaveProfile()
    {
        val currentState = state.value
        val validGoals = listOf("lose_weight", "maintain_weight", "gain_weight")
        val nameErrorRes = if (currentState.name.trim().isBlank()) R.string.error_enter_full_name else null
        val dateOfBirthErrorRes = when
        {
            currentState.dateOfBirth.isBlank() -> R.string.error_select_date_of_birth
            else -> runCatching { validateDateOfBirth(currentState.dateOfBirth) }.exceptionOrNull()
                ?.let { R.string.error_enter_valid_dob }
        }
        val genderErrorRes = if (currentState.gender.isBlank()) R.string.error_select_gender else null
        val weightErrorRes = validateNumberField(
            currentState.weight,
            R.string.error_enter_weight,
            R.string.error_weight_number,
            R.string.error_weight_between,
            20.0,
            300.0
        )
        val heightErrorRes = validateNumberField(
            currentState.height,
            R.string.error_enter_height,
            R.string.error_height_number,
            R.string.error_height_between,
            80.0,
            250.0
        )
        val activityLevelErrorRes =
            if (currentState.activityLevel !in 1f..5f) R.string.error_activity_level_invalid else null
        val goalErrorRes = if (currentState.goal !in validGoals) R.string.error_select_goal else null

        if (listOf(
                nameErrorRes,
                dateOfBirthErrorRes,
                genderErrorRes,
                weightErrorRes,
                heightErrorRes,
                activityLevelErrorRes,
                goalErrorRes
            ).any { it != null }
        ) {
            updateState {
                it.copy(
                    isSaving = false,
                    errorMessageRes = null,
                    isSuccess = false,
                    nameErrorRes = nameErrorRes,
                    dateOfBirthErrorRes = dateOfBirthErrorRes,
                    genderErrorRes = genderErrorRes,
                    weightErrorRes = weightErrorRes,
                    heightErrorRes = heightErrorRes,
                    activityLevelErrorRes = activityLevelErrorRes,
                    goalErrorRes = goalErrorRes
                )
            }
            return
        }

        updateState { it.copy(isSaving = true, errorMessageRes = null, isSuccess = false) }

        launchSafe(
            onError = {
                updateState { it.copy(isSaving = false, errorMessageRes = R.string.error_profile_saving) }
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

            updateState { it.copy(isSaving = false, isSuccess = true, errorMessageRes = null) }
            sendEffect(UpdateProfileEffect.ShowSuccess)
            sendEffect(UpdateProfileEffect.NavigateBack)
        }
    }

    @StringRes
    private fun validateNumberField(
        value: String,
        @StringRes blankErrorRes: Int,
        @StringRes numberErrorRes: Int,
        @StringRes rangeErrorRes: Int,
        min: Double,
        max: Double
    ): Int? {
        if (value.isBlank()) return blankErrorRes
        val number = value.toDoubleOrNull() ?: return numberErrorRes
        return if (number in min..max) null else rangeErrorRes
    }
}
