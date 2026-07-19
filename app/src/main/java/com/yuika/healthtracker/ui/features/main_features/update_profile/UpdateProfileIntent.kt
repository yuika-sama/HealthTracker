package com.yuika.healthtracker.ui.features.main_features.update_profile

import com.yuika.healthtracker.ui.core.base.UiIntent

sealed class UpdateProfileIntent : UiIntent {
    object LoadProfile : UpdateProfileIntent()
    data class UpdateName(val name: String) : UpdateProfileIntent()
    data class UpdateDateOfBirth(val dateOfBirth: String) : UpdateProfileIntent()
    data class UpdateGender(val gender: String) : UpdateProfileIntent()
    data class UpdateWeight(val weight: String) : UpdateProfileIntent()
    data class UpdateHeight(val height: String) : UpdateProfileIntent()
    data class UpdateActivityLevel(val level: Float) : UpdateProfileIntent()
    data class UpdateGoal(val goal: String) : UpdateProfileIntent()
    object SaveProfile : UpdateProfileIntent()
    data class UpdateAvatar(val avatarPath: String): UpdateProfileIntent()
}
