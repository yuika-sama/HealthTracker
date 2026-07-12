package com.yuika.healthtracker.ui.features.main_features.onboarding.page4

import com.yuika.healthtracker.domain.repository.UserRepository
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class OnboardingPage4ViewModel @Inject constructor(
    private val userRepository: UserRepository
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
            val user = userRepository.getLatestUserFlow().firstOrNull()
            if (user != null) {
                // Calculation logic
                val weight = user.weight
                val height = user.height
                val age = user.age
                
                var bmr = (10 * weight) + (6.25 * height) - (5 * age)
                bmr += if (user.gender == "Male") 5.0 else -161.0
                
                val activityText: String
                val activityMultiplier = when (user.activityLevel) {
                    "sedentary" -> { activityText = "1.2x Sedentary"; 1.2 }
                    "lightly_active" -> { activityText = "1.375x Lightly Active"; 1.375 }
                    "moderately_active" -> { activityText = "1.55x Active"; 1.55 }
                    "very_active" -> { activityText = "1.725x Very Active"; 1.725 }
                    "extra_active" -> { activityText = "1.9x Extra Active"; 1.9 }
                    else -> { activityText = "1.55x Active"; 1.55 }
                }
                
                var tdee = bmr * activityMultiplier
                
                when (user.goal) {
                    "lose_weight" -> tdee -= 500
                    "gain_weight" -> tdee += 500
                }
                
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
                        bmr = bmr.toInt(),
                        tdee = tdee.toInt(),
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
