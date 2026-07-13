package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class ProfileEffect : UiEffect{
    object NavigateToLogin : ProfileEffect()
    data class ShowError(val message: String) : ProfileEffect()
}
