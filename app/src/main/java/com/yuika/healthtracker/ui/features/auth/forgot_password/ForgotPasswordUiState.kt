package com.yuika.healthtracker.ui.features.auth.forgot_password

import com.yuika.healthtracker.ui.core.base.UiState

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
) : UiState
