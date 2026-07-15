package com.yuika.healthtracker.ui.features.main_features.add_meal

import com.yuika.healthtracker.ui.core.base.UiEffect

sealed class AddMealEffect : UiEffect
{
    object NavigateBackWithSuccess: AddMealEffect()
}
