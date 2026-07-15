package com.yuika.healthtracker.ui.features.auth.password_changed

import com.yuika.healthtracker.ui.core.base.UiState

data class PasswordChangedUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) : UiState
