package com.yuika.healthtracker.ui.features.main_features.update_profile

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class UpdateProfileEffect : UiEffect
{
    object NavigateBack : UpdateProfileEffect()
    object ShowSuccess : UpdateProfileEffect()
}
