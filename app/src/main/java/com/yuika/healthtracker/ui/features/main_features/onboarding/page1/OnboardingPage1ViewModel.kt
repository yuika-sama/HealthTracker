package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import androidx.annotation.StringRes
import com.yuika.healthtracker.R
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
            is OnboardingPage1Intent.NameChanged -> updateState { it.copy(name = intent.name, nameErrorRes = null) }
            is OnboardingPage1Intent.DateOfBirthChanged -> updateState {
                it.copy(dateOfBirth = intent.dateOfBirth, dateOfBirthErrorRes = null)
            }
            is OnboardingPage1Intent.GenderChanged -> updateState { it.copy(gender = intent.gender, genderErrorRes = null) }
            is OnboardingPage1Intent.WeightChanged -> updateState { it.copy(weight = intent.weight, weightErrorRes = null) }
            is OnboardingPage1Intent.HeightChanged -> updateState { it.copy(height = intent.height, heightErrorRes = null) }
            is OnboardingPage1Intent.Submit -> validateAndSave()
        }
    }

    private fun validateAndSave() {
        val currentState = state.value
        val validatedState = currentState.withFieldErrors()

        if (validatedState.hasFieldErrors()) {
            updateState { validatedState.copy(errorMessageRes = null, isLoading = false, isSuccess = false) }
            return
        }

        updateState {
            it.copy(
                isLoading = true,
                errorMessageRes = null,
                isSuccess = false,
                nameErrorRes = null,
                dateOfBirthErrorRes = null,
                genderErrorRes = null,
                weightErrorRes = null,
                heightErrorRes = null
            )
        }

        launchSafe(
            onError = {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessageRes = R.string.error_save_information,
                        isSuccess = false
                    )
                }
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
            updateState { it.copy(isLoading = false, isSuccess = true, errorMessageRes = null) }
            sendEffect(OnboardingPage1Effect.NavigateToPage2)
        }
    }

    private fun OnboardingPage1UiState.withFieldErrors(): OnboardingPage1UiState {
        return copy(
            nameErrorRes = if (name.trim().isBlank()) R.string.error_enter_full_name else null,
            dateOfBirthErrorRes = validateDateField(dateOfBirth),
            genderErrorRes = if (gender.isBlank()) R.string.error_select_gender else null,
            weightErrorRes = validateNumberField(
                weight,
                R.string.error_enter_weight,
                R.string.error_weight_number,
                R.string.error_weight_between,
                20.0,
                300.0
            ),
            heightErrorRes = validateNumberField(
                height,
                R.string.error_enter_height,
                R.string.error_height_number,
                R.string.error_height_between,
                80.0,
                250.0
            )
        )
    }

    private fun OnboardingPage1UiState.hasFieldErrors(): Boolean {
        return listOf(nameErrorRes, dateOfBirthErrorRes, genderErrorRes, weightErrorRes, heightErrorRes)
            .any { it != null }
    }

    @StringRes
    private fun validateDateField(value: String): Int? {
        if (value.isBlank()) return R.string.error_select_date_of_birth
        return runCatching { validateDateOfBirth(value) }.exceptionOrNull()
            ?.let { R.string.error_enter_valid_dob }
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
