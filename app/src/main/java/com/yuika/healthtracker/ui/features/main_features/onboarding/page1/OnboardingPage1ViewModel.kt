package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.ValidateAndSaveOnboardingUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import com.yuika.healthtracker.utils.NETWORK_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class OnboardingPage1ViewModel @Inject constructor(
    private val validateAndSaveOnboardingUseCase: ValidateAndSaveOnboardingUseCase
) : BaseViewModel<OnboardingPage1UiState, OnboardingPage1Intent, OnboardingPage1Effect>(
    initialState = OnboardingPage1UiState()
) {
    override fun onIntent(intent: OnboardingPage1Intent) {
        when (intent) {
            is OnboardingPage1Intent.NameChanged -> updateState { it.copy(name = intent.name, errorMessage = null) }
            is OnboardingPage1Intent.AgeChanged -> updateState { it.copy(age = intent.age, errorMessage = null) }
            is OnboardingPage1Intent.GenderChanged -> updateState { it.copy(gender = intent.gender, errorMessage = null) }
            is OnboardingPage1Intent.WeightChanged -> updateState { it.copy(weight = intent.weight, errorMessage = null) }
            is OnboardingPage1Intent.HeightChanged -> updateState { it.copy(height = intent.height, errorMessage = null) }
            is OnboardingPage1Intent.Submit -> validateAndSave()
        }
    }

    private fun validateAndSave() {
        val currentState = state.value

        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }

        launchSafe(
            onError = { throwable ->
                val errorMsg = throwable.message ?: "Error saving information"
                updateState { it.copy(isLoading = false, errorMessage = errorMsg) }
                sendEffect(OnboardingPage1Effect.ShowError(errorMsg))
            }
        ) {
            validateAndSaveOnboardingUseCase(
                name = currentState.name,
                ageStr = currentState.age,
                gender = currentState.gender,
                weightStr = currentState.weight,
                heightStr = currentState.height
            )
            
            delay(NETWORK_DELAY.toLong())
            updateState { it.copy(isLoading = false, isSuccess = true) }
            sendEffect(OnboardingPage1Effect.NavigateToPage2)
        }
    }
}
