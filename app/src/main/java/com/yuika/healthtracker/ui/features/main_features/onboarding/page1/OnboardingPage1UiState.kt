package com.yuika.healthtracker.ui.features.main_features.onboarding.page1

import androidx.annotation.StringRes
import com.yuika.healthtracker.ui.core.base.UiState

data class OnboardingPage1UiState( 
    val isLoading: Boolean = false,
    val name: String = "",
    val dateOfBirth: String = "",
    val gender: String = "Male",
    val weight: String = "",
    val height: String = "",
    @StringRes val nameErrorRes: Int? = null,
    @StringRes val dateOfBirthErrorRes: Int? = null,
    @StringRes val genderErrorRes: Int? = null,
    @StringRes val weightErrorRes: Int? = null,
    @StringRes val heightErrorRes: Int? = null,
    @StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState
