package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage1UiState( 
    val isLoading: Boolean = false,
    val name: String = "",
    val age: String = "",
    val gender: String = "Male",
    val weight: String = "",
    val height: String = "",
    val errorMessage: String? = null
) : UiState

