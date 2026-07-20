package com.yuika.healthtracker.ui.features.main_features.onboarding.page2

import com.yuika.healthtracker.R
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
            is OnboardingPage2Intent.ActivityLevelChanged -> updateState {
                it.copy(activityLevel = intent.level, activityLevelErrorRes = null)
            }
            is OnboardingPage2Intent.Submit -> saveAndNavigate()
        }
    }

    private fun saveAndNavigate() {
        val currentLevel = state.value.activityLevel
        if (currentLevel !in validActivityLevels) {
            updateState {
                it.copy(
                    isLoading = false,
                    activityLevelErrorRes = R.string.error_select_activity_level,
                    errorMessageRes = null,
                    isSuccess = false
                )
            }
            return
        }

        updateState {
            it.copy(
                isLoading = true,
                activityLevelErrorRes = null,
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
                val updatedUser = user.copy(activityLevel = currentLevel)
                updateUserUseCase(updatedUser)
                runCatching {
                    widgetService.refresh()
                }
                updateState { it.copy(isLoading = false, isSuccess = true, errorMessageRes = null) }
                sendEffect(OnboardingPage2Effect.NavigateToPage3)
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
        val validActivityLevels = setOf(
            "sedentary",
            "lightly_active",
            "moderately_active",
            "very_active",
            "extra_active"
        )
    }
}
