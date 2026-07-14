package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.data.local.entity.ActivityEntity
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.ui.core.model.IntensityLevel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ValidateAndSaveActivityUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveActivityUseCase: SaveActivityUseCase
) {
    suspend operator fun invoke(
        activityName: String,
        selectedIcon: String,
        kcalPerHourStr: String,
        durationStr: String,
        selectedIntensity: IntensityLevel,
        estimatedKcalBurned: Int,
        dateText: String
    ) {
        val name = activityName.trim()
        if (name.isEmpty()) {
            throw IllegalArgumentException("Please fill in your name")
        }
        val kcalPerHour = kcalPerHourStr.toIntOrNull()
        if (kcalPerHour == null || kcalPerHour <= 0) {
            throw IllegalArgumentException("Please fill in valid kcal/hour")
        }
        val duration = durationStr.toIntOrNull()
        if (duration == null || duration <= 0) {
            throw IllegalArgumentException("Please fill in valid practice duration")
        }

        val user = getLatestUserUseCase().firstOrNull()
        if (user == null){
            throw IllegalStateException("Can't find user information")
        }

        val activityEntity = ActivityEntity(
            userId = user.id,
            name = name,
            iconName = selectedIcon,
            kcalPerHour = kcalPerHour,
            durationMins = duration,
            intensity = selectedIntensity.name,
            kcalBurned = estimatedKcalBurned,
            dateText = dateText
        )

        saveActivityUseCase(activityEntity)
    }
}
