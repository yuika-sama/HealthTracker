package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class OnboardingPage1Effect : UiEffect {
    object NavigateToPage2 : OnboardingPage1Effect()
    object NavigateBack : OnboardingPage1Effect()
    data class ShowError(val message: String) : OnboardingPage1Effect()
}

