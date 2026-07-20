package com.yuika.healthtracker.ui.features.main_features.update_profile

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
    val nameError: String? = null,
    val dateOfBirthError: String? = null,
    val genderError: String? = null,
    val weightError: String? = null,
    val heightError: String? = null,
    val activityLevelError: String? = null,
    val goalError: String? = null,
    
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState
