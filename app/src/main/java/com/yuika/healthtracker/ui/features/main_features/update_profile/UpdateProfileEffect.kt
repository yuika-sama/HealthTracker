package com.yuika.healthtracker.ui.features.main_features.update_profile

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class UpdateProfileEffect : UiEffect
{
    object NavigateBack : UpdateProfileEffect()
    data class ShowError(val message: String) : UpdateProfileEffect()
    data class ShowSuccess(val message: String) : UpdateProfileEffect()
}
