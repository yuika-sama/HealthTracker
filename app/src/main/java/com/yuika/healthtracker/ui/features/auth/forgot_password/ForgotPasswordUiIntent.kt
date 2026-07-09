package com.yuika.healthtracker.ui.features.auth.forgot_password

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class ForgotPasswordUiIntent : UiIntent
{
    data class EmailChanged(val email: String) : ForgotPasswordUiIntent()
    object SubmitClick : ForgotPasswordUiIntent()
    object LoginClick : ForgotPasswordUiIntent()
}