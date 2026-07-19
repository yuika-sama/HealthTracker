package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage1UiState( 
    val isLoading: Boolean = false,
    val name: String = "",
    val dateOfBirth: String = "",
    val gender: String = "Male",
    val weight: String = "",
    val height: String = "",
    val nameError: String? = null,
    val dateOfBirthError: String? = null,
    val genderError: String? = null,
    val weightError: String? = null,
    val heightError: String? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState

