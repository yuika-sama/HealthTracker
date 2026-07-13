package com.yuika.healthtracker.ui.features.main_features.trends

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class TrendsEffect: UiEffect
{
    data class ShowError(val message: String) : TrendsEffect()
}