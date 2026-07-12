package com.yuika.healthtracker.ui.features.main_features.onboarding.page4

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class OnboardingPage4Effect : UiEffect {
    object NavigateToDashboard : OnboardingPage4Effect()
    data class ShowError(val message: String) : OnboardingPage4Effect()
}

