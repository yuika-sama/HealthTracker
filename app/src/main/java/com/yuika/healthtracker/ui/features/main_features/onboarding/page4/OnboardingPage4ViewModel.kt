package com.yuika.healthtracker.ui.features.main_features.onboarding.page4

import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class OnboardingPage4ViewModel @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
) : BaseViewModel<OnboardingPage4UiState, OnboardingPage4Intent, OnboardingPage4Effect>(
    initialState = OnboardingPage4UiState()
) {
    init {
        onIntent(OnboardingPage4Intent.LoadData)
    }

    override fun onIntent(intent: OnboardingPage4Intent) {
        when (intent) {
            is OnboardingPage4Intent.LoadData -> calculateMetrics()
            is OnboardingPage4Intent.CompleteOnboarding -> completeOnboarding()
        }
    }

    private fun calculateMetrics() {
        updateState { it.copy(isLoading = true) }
        launchSafe(
            onError = { throwable ->
                updateState { it.copy(isLoading = false, errorMessage = throwable.message) }
            }
        ) {
            val user = getLatestUserUseCase().firstOrNull()
            if (user != null) {
                val stats = calculateUserStatsUseCase(user)
                
                val activityText = when (user.activityLevel) {
                    "sedentary" -> "1.2x Sedentary"
                    "lightly_active" -> "1.375x Lightly Active"
                    "moderately_active" -> "1.55x Active"
                    "very_active" -> "1.725x Very Active"
                    "extra_active" -> "1.9x Extra Active"
                    else -> "1.55x Active"
                }
                
                val tdee = stats.tdee
                
                // Macros
                // Protein: 30%, Fat: 25%, Carbs: 45%
                val proteinCals = tdee * 0.30
                val fatCals = tdee * 0.25
                val carbsCals = tdee * 0.45
                
                val proteinGrams = (proteinCals / 4).toInt()
                val fatGrams = (fatCals / 9).toInt()
                val carbsGrams = (carbsCals / 4).toInt()
                
                updateState {
                    it.copy(
                        isLoading = false,
                        bmr = stats.bmr.toInt(),
                        tdee = stats.goalKcal,
                        proteinGrams = proteinGrams,
                        fatGrams = fatGrams,
                        carbsGrams = carbsGrams,
                        activityMultiplierText = activityText
                    )
                }
            } else {
                updateState { it.copy(isLoading = false, errorMessage = "User not found") }
            }
        }
    }

    private fun completeOnboarding() {
        sendEffect(OnboardingPage4Effect.NavigateToDashboard)
    }
}
