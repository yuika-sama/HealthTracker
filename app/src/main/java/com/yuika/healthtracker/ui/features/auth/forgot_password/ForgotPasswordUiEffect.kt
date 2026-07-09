package com.yuika.healthtracker.ui.features.auth.forgot_password

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class ForgotPasswordUiEffect : UiEffect
{
    data class NavigateToVerifyOtp(val email: String) : ForgotPasswordUiEffect()
    object NavigateToLogin : ForgotPasswordUiEffect()
    data class ShowToast(val message: String) : ForgotPasswordUiEffect()
}