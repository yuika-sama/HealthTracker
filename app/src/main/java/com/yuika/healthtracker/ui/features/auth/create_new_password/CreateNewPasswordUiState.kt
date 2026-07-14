package com.yuika.healthtracker.ui.features.auth.create_new_password

import com.yuika.healthtracker.ui.core.base.UiState

data class CreateNewPasswordUiState(
    val newPassword: String = "",
    val newPasswordError: String? = null,
    val isShowNewPassword: Boolean = false,
    val newPasswordStrength: Int = 0,
    val confirmNewPassword: String = "",
    val confirmNewPasswordError: String? = null,
    val isShowConfirmNewPassword: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
) : UiState
