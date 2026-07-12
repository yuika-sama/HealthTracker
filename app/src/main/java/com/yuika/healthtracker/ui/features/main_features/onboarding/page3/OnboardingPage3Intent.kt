package com.yuika.healthtracker.ui.features.main_features.onboarding.page3

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class OnboardingPage3Intent : UiIntent {
    data class GoalChanged(val goal: String) : OnboardingPage3Intent()
    object Submit : OnboardingPage3Intent()
}

