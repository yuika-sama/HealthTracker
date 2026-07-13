package com.yuika.healthtracker.ui.features.main_features.activity

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class ActivityEffect : UiEffect
{
    data class ShowError(val message: String) : ActivityEffect()
    object NavigateToAddActivity : ActivityEffect()
}