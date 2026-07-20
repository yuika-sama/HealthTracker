package com.yuika.healthtracker.ui.features.main_features.onboarding.page3

import com.yuika.healthtracker.R
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.UpdateUserUseCase
import com.yuika.healthtracker.service.widget.WidgetService
import com.yuika.healthtracker.ui.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class OnboardingPage3ViewModel @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val widgetService: WidgetService
) : BaseViewModel<OnboardingPage3UiState, OnboardingPage3Intent, OnboardingPage3Effect>(
    initialState = OnboardingPage3UiState()
) {
    override fun onIntent(intent: OnboardingPage3Intent) {
        when (intent) {
            is OnboardingPage3Intent.GoalChanged -> updateState { it.copy(goal = intent.goal, goalErrorRes = null) }
            is OnboardingPage3Intent.Submit -> saveAndNavigate()
        }
    }

    private fun saveAndNavigate() {
        val currentGoal = state.value.goal
        if (currentGoal !in validGoals) {
            updateState {
                it.copy(
                    isLoading = false,
                    goalErrorRes = R.string.error_select_goal,
                    errorMessageRes = null,
                    isSuccess = false
                )
            }
            return
        }

        updateState {
            it.copy(
                isLoading = true,
                goalErrorRes = null,
                errorMessageRes = null,
                isSuccess = false
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
            val user = getLatestUserUseCase().firstOrNull()
            if (user != null) {
                val updatedUser = user.copy(goal = currentGoal)
                updateUserUseCase(updatedUser)
                runCatching {
                    widgetService.refresh()
                }
                updateState { it.copy(isLoading = false, isSuccess = true, errorMessageRes = null) }
                sendEffect(OnboardingPage3Effect.NavigateToPage4)
            } else {
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

    private companion object {
        val validGoals = setOf("lose_weight", "maintain_weight", "gain_weight")
    }
}
