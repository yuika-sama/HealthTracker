package com.yuika.healthtracker.ui.features.main_features.onboarding.page2

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class OnboardingPage2Intent : UiIntent {
    data class ActivityLevelChanged(val level: String) : OnboardingPage2Intent()
    object Submit : OnboardingPage2Intent()
}

