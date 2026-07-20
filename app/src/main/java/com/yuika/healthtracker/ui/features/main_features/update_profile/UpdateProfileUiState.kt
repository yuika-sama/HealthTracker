package com.yuika.healthtracker.ui.features.main_features.update_profile

import androidx.annotation.StringRes
import com.yuika.healthtracker.ui.core.base.UiState

data class UpdateProfileUiState(
    val id: Int = 0,
    val name: String = "",
    val dateOfBirth: String = "",
    val gender: String = "Male",
    val weight: String = "",
    val height: String = "",
    val activityLevel: Float = 3f,
    val goal: String = "lose_weight",
    val avatarPath: String? = null,
    val createdAt: Long = 0L,
    @StringRes val nameErrorRes: Int? = null,
    @StringRes val dateOfBirthErrorRes: Int? = null,
    @StringRes val genderErrorRes: Int? = null,
    @StringRes val weightErrorRes: Int? = null,
    @StringRes val heightErrorRes: Int? = null,
    @StringRes val activityLevelErrorRes: Int? = null,
    @StringRes val goalErrorRes: Int? = null,
    
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    @StringRes val errorMessageRes: Int? = null,
    val isSuccess: Boolean = false
) : UiState
