package com.yuika.healthtracker.ui.features.auth.create_new_password

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class CreateNewPasswordIntent : UiIntent
{
    data class NewPasswordChanged(val password: String) : CreateNewPasswordIntent()
    object ShowNewPassword : CreateNewPasswordIntent()
    data class NewPasswordStrengthChanged(val strength: Int) : CreateNewPasswordIntent()
    data class ConfirmNewPasswordChanged(val confirmPassword: String): CreateNewPasswordIntent()
    object ShowConfirmNewPassword: CreateNewPasswordIntent()
    object ResetPasswordClick : CreateNewPasswordIntent()
}