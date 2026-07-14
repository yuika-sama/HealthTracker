package com.yuika.healthtracker.ui.features.main_features.onboarding.page3

import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage3UiState( 
    val isLoading: Boolean = false,
    val goal: String = "lose_weight",
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState

