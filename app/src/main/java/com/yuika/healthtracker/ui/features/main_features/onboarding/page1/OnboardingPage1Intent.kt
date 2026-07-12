package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class OnboardingPage1Intent : UiIntent {
    data class NameChanged(val name: String) : OnboardingPage1Intent()
    data class AgeChanged(val age: String) : OnboardingPage1Intent()
    data class GenderChanged(val gender: String) : OnboardingPage1Intent()
    data class WeightChanged(val weight: String) : OnboardingPage1Intent()
    data class HeightChanged(val height: String) : OnboardingPage1Intent()
    object Submit : OnboardingPage1Intent()
}

