package com.yuika.healthtracker.ui.features.main_features.add_activity

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class AddActivityEffect : UiEffect
{
    data class ShowError(val message: String) : AddActivityEffect()
    object NavigateToActivity: AddActivityEffect()
}