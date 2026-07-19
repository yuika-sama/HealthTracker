package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.ValidateAndSaveOnboardingUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.validateDateOfBirth
import com.yuika.healthtracker.service.widget.WidgetService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingPage1ViewModel @Inject constructor(
    private val validateAndSaveOnboardingUseCase: ValidateAndSaveOnboardingUseCase,
    private val widgetService: WidgetService
) : BaseViewModel<OnboardingPage1UiState, OnboardingPage1Intent, OnboardingPage1Effect>(
    initialState = OnboardingPage1UiState()
) {
    override fun onIntent(intent: OnboardingPage1Intent) {
        when (intent) {
            is OnboardingPage1Intent.NameChanged -> updateState { it.copy(name = intent.name, nameError = null) }
            is OnboardingPage1Intent.DateOfBirthChanged -> updateState {
                it.copy(dateOfBirth = intent.dateOfBirth, dateOfBirthError = null)
            }
            is OnboardingPage1Intent.GenderChanged -> updateState { it.copy(gender = intent.gender, genderError = null) }
            is OnboardingPage1Intent.WeightChanged -> updateState { it.copy(weight = intent.weight, weightError = null) }
            is OnboardingPage1Intent.HeightChanged -> updateState { it.copy(height = intent.height, heightError = null) }
            is OnboardingPage1Intent.Submit -> validateAndSave()
        }
    }

    private fun validateAndSave() {
        val currentState = state.value
        val validatedState = currentState.withFieldErrors()

        if (validatedState.hasFieldErrors()) {
            updateState { validatedState.copy(errorMessage = null, isLoading = false, isSuccess = false) }
            return
        }

        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null,
                isSuccess = false,
                nameError = null,
                dateOfBirthError = null,
                genderError = null,
                weightError = null,
                heightError = null
            )
        }

        launchSafe(
            onError = { throwable ->
                val errorMsg = throwable.message ?: "Error saving information"
                updateState { it.copy(isLoading = false, errorMessage = errorMsg, isSuccess = false) }
            }
        ) {
            validateAndSaveOnboardingUseCase(
                name = currentState.name,
                dateOfBirth = currentState.dateOfBirth,
                gender = currentState.gender,
                weightStr = currentState.weight,
                heightStr = currentState.height
            )
            runCatching{
                widgetService.refresh()
            }
            updateState { it.copy(isLoading = false, isSuccess = true, errorMessage = null) }
            sendEffect(OnboardingPage1Effect.NavigateToPage2)
        }
    }

    private fun OnboardingPage1UiState.withFieldErrors(): OnboardingPage1UiState {
        return copy(
            nameError = if (name.trim().isBlank()) "Please enter your full name" else null,
            dateOfBirthError = validateDateField(dateOfBirth),
            genderError = if (gender.isBlank()) "Please select your gender" else null,
            weightError = validateNumberField(weight, "Weight", 20.0, 300.0, "kg"),
            heightError = validateNumberField(height, "Height", 80.0, 250.0, "cm")
        )
    }

    private fun OnboardingPage1UiState.hasFieldErrors(): Boolean {
        return listOf(nameError, dateOfBirthError, genderError, weightError, heightError).any { it != null }
    }

    private fun validateDateField(value: String): String? {
        if (value.isBlank()) return "Please select your date of birth"
        return runCatching { validateDateOfBirth(value) }.exceptionOrNull()?.message
    }

    private fun validateNumberField(
        value: String,
        label: String,
        min: Double,
        max: Double,
        unit: String
    ): String? {
        if (value.isBlank()) return "Please enter your ${label.lowercase()}"
        val number = value.toDoubleOrNull() ?: return "$label must be a number"
        return if (number in min..max) null else "$label must be between ${min.toInt()} and ${max.toInt()} $unit"
    }
}
