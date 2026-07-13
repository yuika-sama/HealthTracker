package com.yuika.healthtracker.ui.features.main_features.add_activity

import com.yuika.healthtracker.ui.core.base.UiIntent
import java.time.LocalDate

sealed class AddActivityIntent : UiIntent
{
    data class Init(val dateText: String = LocalDate.now().toString()) : AddActivityIntent()
    data class OnActivityNameChange(val name: String) : AddActivityIntent()
    data class OnIconChange(val iconName: String) : AddActivityIntent()
    data class OnKcalPerHourChange(val kcal: String) : AddActivityIntent()
    data class OnDurationChange(val duration: String) : AddActivityIntent()
    data class OnIntensityChange(val intensity: String) : AddActivityIntent()
    object OnSaveClick: AddActivityIntent()
}