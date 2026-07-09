package com.yuika.healthtracker.ui.features.auth.otpverify

import com.yuika.healthtracker.ui.core.base.UiState

data class OtpVerifyUiState(
    val email: String = "",
    val otpCode: String = "",
    val otpLength: Int = 6,
    val isLoading: Boolean = false,
    val otpError: String? = null,
    val errorMessage: String? = null
) : UiState