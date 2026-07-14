package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.ui.core.base.UiState

data class ProfileUiState(
    val name: String = "",
    val subtitle: String = "",
    val weight: String = "",
    val height: String = "",
    val bmi: String = "",
    val goalTitle: String = "Current Goal",
    val goalDescription: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState
