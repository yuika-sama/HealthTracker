package com.yuika.healthtracker.ui.features.main_features.onboarding.page4

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class OnboardingPage4Intent : UiIntent {
    object LoadData : OnboardingPage4Intent()
    object CompleteOnboarding : OnboardingPage4Intent()
}

