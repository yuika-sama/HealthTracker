package com.yuika.healthtracker.ui.features.main_features.onboarding.page4

import androidx.annotation.StringRes
import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage4UiState( 
    val isLoading: Boolean = false,
    val bmr: Int = 0,
    val tdee: Int = 0,
    val proteinGrams: Int = 0,
    val fatGrams: Int = 0,
    val carbsGrams: Int = 0,
    val activityMultiplierText: String = "",
    @StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState
