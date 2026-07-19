package com.yuika.healthtracker.ui.features.main_features.onboarding.page2

import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage2UiState( 
    val isLoading: Boolean = false,
    val activityLevel: String = "moderately_active",
    val activityLevelError: String? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState

