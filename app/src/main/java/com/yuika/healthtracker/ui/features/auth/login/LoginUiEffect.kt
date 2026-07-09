package com.yuika.healthtracker.ui.features.auth.login

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class LoginUiEffect : UiEffect
{
    object NavigateToDashboard : LoginUiEffect()
    object NavigateToRegister : LoginUiEffect()
    object NavigateToForgotPassword : LoginUiEffect()
    data class ShowToast(val message: String) : LoginUiEffect()
}