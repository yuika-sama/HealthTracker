package com.yuika.healthtracker.ui.features.main_features.onboarding.page3

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class OnboardingPage3ViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<OnboardingPage3UiState, OnboardingPage3Intent, OnboardingPage3Effect>(
    initialState = OnboardingPage3UiState()
) {
    override fun onIntent(intent: OnboardingPage3Intent) {
        when (intent) {
            is OnboardingPage3Intent.GoalChanged -> updateState { it.copy(goal = intent.goal) }
            is OnboardingPage3Intent.Submit -> saveAndNavigate()
        }
    }

    private fun saveAndNavigate() {
        updateState { it.copy(isLoading = true) }
        val currentGoal = state.value.goal

        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
                sendEffect(OnboardingPage3Effect.ShowError(throwable.message ?: "Error saving information"))
            }
        ) {
            val user = userRepository.getLatestUserFlow().firstOrNull()
            if (user != null) {
                val updatedUser = user.copy(goal = currentGoal)
                userRepository.updateUser(updatedUser)
                updateState { it.copy(isLoading = false) }
                sendEffect(OnboardingPage3Effect.NavigateToPage4)
            } else {
                updateState { it.copy(isLoading = false, errorMessage = "User not found") }
                sendEffect(OnboardingPage3Effect.ShowError("User not found"))
            }
        }
    }
}
