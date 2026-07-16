package com.yuika.healthtracker.ui.features.main_features.add_activity

import com.yuika.healthtracker.domain.model.ActivityCatalog
import com.yuika.healthtracker.ui.core.base.UiIntent
import java.time.LocalDate

sealed class AddActivityIntent : UiIntent
{
    data class Init(val dateText: String = LocalDate.now().toString()) : AddActivityIntent()
    data class OnActivitySelected(val activity: ActivityCatalog) : AddActivityIntent()
    data class OnDurationChange(val duration: String) : AddActivityIntent()
    object OnSaveClick: AddActivityIntent()
}
