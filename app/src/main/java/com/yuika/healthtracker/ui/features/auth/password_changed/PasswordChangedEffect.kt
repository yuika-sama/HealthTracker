package com.yuika.healthtracker.ui.features.auth.password_changed

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class PasswordChangedEffect : UiEffect
{
    object NavigateToLogin : PasswordChangedEffect()
}
