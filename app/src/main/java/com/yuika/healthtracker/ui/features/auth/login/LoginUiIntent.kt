package com.yuika.healthtracker.ui.features.auth.login

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed interface LoginUiIntent : UiIntent
{
    data class EmailChanged(val email: String) : LoginUiIntent
    data class PasswordChanged(val password: String) : LoginUiIntent
    object LoginClick : LoginUiIntent
    object ShowPasswordClick : LoginUiIntent
    object RememberAccountClick : LoginUiIntent
    object ForgotPasswordClick : LoginUiIntent
    object GoogleClick : LoginUiIntent
    object FacebookClick : LoginUiIntent
    object RegisterClick : LoginUiIntent
}