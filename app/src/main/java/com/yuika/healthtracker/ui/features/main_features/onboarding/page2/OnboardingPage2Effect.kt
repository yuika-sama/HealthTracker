package com.yuika.healthtracker.ui.features.main_features.onboarding.page2

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class OnboardingPage2Effect : UiEffect {
    object NavigateToPage3 : OnboardingPage2Effect()
    object NavigateBack : OnboardingPage2Effect()
    data class ShowError(val message: String) : OnboardingPage2Effect()
}

