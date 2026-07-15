package com.yuika.healthtracker.ui.features.main_features.onboarding.page3

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class OnboardingPage3Effect : UiEffect {
    object NavigateToPage4 : OnboardingPage3Effect()
    object NavigateBack : OnboardingPage3Effect()
}

