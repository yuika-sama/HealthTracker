package com.yuika.healthtracker.ui.features.auth.register

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class RegisterIntent : UiIntent
{
    data class FullNameChanged(val fullName: String): RegisterIntent()
    data class EmailChanged(val email: String): RegisterIntent()
    data class PasswordChanged(val password: String): RegisterIntent()
    data class ConfirmPasswordChanged(val confirmPassword: String): RegisterIntent()
    object ShowPasswordChanged: RegisterIntent()
    object ShowConfirmPasswordChanged: RegisterIntent()
    object AgreedToTermsChanged: RegisterIntent()
    object Submit: RegisterIntent()
}