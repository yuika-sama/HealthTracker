package com.yuika.healthtracker.ui.features.auth.login

import com.yuika.healthtracker.ui.core.base.UiState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val errorMessage: String? = null,
    val isShowPassword: Boolean = false,
    val isRememberAccount: Boolean = false,
    val isLoading: Boolean = false,
    val passwordLength: Int = 8
) : UiState
