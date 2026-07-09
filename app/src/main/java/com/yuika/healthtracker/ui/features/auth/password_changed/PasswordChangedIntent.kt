package com.yuika.healthtracker.ui.features.auth.password_changed

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class PasswordChangedIntent : UiIntent
{
    object BackToLoginClick : PasswordChangedIntent()
}
