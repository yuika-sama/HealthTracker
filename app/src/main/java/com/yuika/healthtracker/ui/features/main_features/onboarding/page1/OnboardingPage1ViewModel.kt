package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import com.yuika.healthtracker.data.local.entity.UserEntity
import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class OnboardingPage1ViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<OnboardingPage1UiState, OnboardingPage1Intent, OnboardingPage1Effect>(
    initialState = OnboardingPage1UiState()
) {
    override fun onIntent(intent: OnboardingPage1Intent) {
        when (intent) {
            is OnboardingPage1Intent.NameChanged -> updateState { it.copy(name = intent.name, errorMessage = null) }
            is OnboardingPage1Intent.DobChanged -> updateState { it.copy(dob = intent.dob, errorMessage = null) }
            is OnboardingPage1Intent.GenderChanged -> updateState { it.copy(gender = intent.gender, errorMessage = null) }
            is OnboardingPage1Intent.WeightChanged -> updateState { it.copy(weight = intent.weight, errorMessage = null) }
            is OnboardingPage1Intent.HeightChanged -> updateState { it.copy(height = intent.height, errorMessage = null) }
            is OnboardingPage1Intent.Submit -> validateAndSave()
        }
    }

    private fun validateAndSave() {
        val currentState = state.value
        if (currentState.name.isBlank() || currentState.weight.isBlank() || currentState.height.isBlank()) {
            updateState { it.copy(errorMessage = "Please let me know your infomation.") }
            sendEffect(OnboardingPage1Effect.ShowError("Please let me know your infomation"))
            return
        }

        val weightValue = currentState.weight.toDoubleOrNull()
        val heightValue = currentState.height.toDoubleOrNull()
        if (weightValue == null || heightValue == null) {
            updateState { it.copy(errorMessage = "Weight or height is not valid") }
            sendEffect(OnboardingPage1Effect.ShowError("Weight or height is not valid"))
            return
        }

        updateState { it.copy(isLoading = true) }

        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(OnboardingPage1Effect.ShowError(throwable.message ?: "Error saving information"))
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()
            if (user != null) {
                val updatedUser = user.copy(
                    name = currentState.name,
                    dob = currentState.dob,
                    gender = currentState.gender,
                    weight = weightValue,
                    height = heightValue
                )
                userRepository.updateUser(updatedUser)
            } else {
                val newUser = UserEntity(
                    email = "dummy@example.com",
                    password = "dummy",
                    name = currentState.name,
                    dob = currentState.dob,
                    gender = currentState.gender,
                    weight = weightValue,
                    height = heightValue,
                    activityLevel = "moderately_active",
                    goal = "lose_weight",
                    avatarPath = null
                )
                userRepository.insertUser(newUser)
            }
            updateState { it.copy(isLoading = false) }
            sendEffect(OnboardingPage1Effect.NavigateToPage2)
        }
    }
}
