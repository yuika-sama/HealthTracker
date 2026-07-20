package com.yuika.healthtracker.ui.features.main_features.onboarding.page3

import androidx.annotation.StringRes
import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage3UiState( 
    val isLoading: Boolean = false,
    val goal: String = "lose_weight",
    @StringRes val goalErrorRes: Int? = null,
    @StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState
