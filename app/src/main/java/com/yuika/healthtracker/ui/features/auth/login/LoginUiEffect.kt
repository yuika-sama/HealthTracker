package com.yuika.healthtracker.ui.features.auth.login

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed interface LoginUiEffect : UiEffect
{
    object NavigateToDashboard: LoginUiEffect
    object NavigateToRegister: LoginUiEffect
    object NavigateToForgotPassword: LoginUiEffect
    data class showToast(val message: String): LoginUiEffect
}