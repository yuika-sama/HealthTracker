package com.yuika.healthtracker.ui.features.main_features.onboarding.page2

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.UpdateUserUseCase
import com.yuika.healthtracker.service.widget.WidgetService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class OnboardingPage2ViewModel @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val widgetService: WidgetService
) : BaseViewModel<OnboardingPage2UiState, OnboardingPage2Intent, OnboardingPage2Effect>(
    initialState = OnboardingPage2UiState()
) {
    override fun onIntent(intent: OnboardingPage2Intent) {
        when (intent) {
            is OnboardingPage2Intent.ActivityLevelChanged -> updateState { it.copy(activityLevel = intent.level) }
            is OnboardingPage2Intent.Submit -> saveAndNavigate()
        }
    }

    private fun saveAndNavigate() {
        updateState { it.copy(isLoading = true, errorMessage = null, isSuccess = false) }
        val currentLevel = state.value.activityLevel

        launchSafe(
            onError = { throwable ->
                val message = throwable.message ?: "Error saving information"
                updateState { it.copy(isLoading = false, errorMessage = message) }
            }
        ) {
            val user = getLatestUserUseCase().firstOrNull()
            if (user != null) {
                val updatedUser = user.copy(activityLevel = currentLevel)
                updateUserUseCase(updatedUser)
                widgetService.refresh()
                updateState { it.copy(isLoading = false, isSuccess = true) }
                sendEffect(OnboardingPage2Effect.NavigateToPage3)
            } else {
                val message = "User not found"
                updateState { it.copy(isLoading = false, errorMessage = message, isSuccess = false) }
            }
        }
    }
}
