package com.yuika.healthtracker.ui.features.main_features.profile

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class ProfileIntent : UiIntent{
    object LoadProfile : ProfileIntent()
    object EditProfile : ProfileIntent()
    object Logout : ProfileIntent()
}
