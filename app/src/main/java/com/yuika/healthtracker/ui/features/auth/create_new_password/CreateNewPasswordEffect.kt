package com.yuika.healthtracker.ui.features.auth.create_new_password

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class CreateNewPasswordEffect : UiEffect
{
    object NavigateToLogin : CreateNewPasswordEffect()
    object NavigateToPasswordChanged : CreateNewPasswordEffect()
    data class ShowToast(val message: String) : CreateNewPasswordEffect()
}