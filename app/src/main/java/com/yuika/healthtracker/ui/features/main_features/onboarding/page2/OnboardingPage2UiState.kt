package com.yuika.healthtracker.ui.features.main_features.onboarding.page2

import androidx.annotation.StringRes
import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage2UiState( 
    val isLoading: Boolean = false,
    val activityLevel: String = "moderately_active",
    @StringRes val activityLevelErrorRes: Int? = null,
    @StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState
