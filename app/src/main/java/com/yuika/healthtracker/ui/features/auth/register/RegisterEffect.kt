package com.yuika.healthtracker.ui.features.auth.register

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class RegisterEffect : UiEffect
{
    data class NavigateToVerifyOtp(val email: String): RegisterEffect()
    data class ShowToast(val message: String): RegisterEffect()
    object NavigateToLogin: RegisterEffect()
}