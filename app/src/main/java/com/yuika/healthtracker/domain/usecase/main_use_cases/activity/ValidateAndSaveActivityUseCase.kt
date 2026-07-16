package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.domain.model.Activity
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import com.yuika.healthtracker.ui.core.model.IntensityLevel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ValidateAndSaveActivityUseCase @Inject constructor(
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveActivityUseCase: SaveActivityUseCase,
    private val validateActivityInputUseCase: ValidateActivityInputUseCase
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
        val validInput = validateActivityInputUseCase(
            activityName = activityName,
            kcalPerHourStr = kcalPerHourStr,
            durationStr = durationStr,
            selectedIntensity = selectedIntensity
        )

        val user = getLatestUserUseCase().firstOrNull()
        if (user == null){
            throw IllegalStateException("Can't find user information")
        }

        val weightKg = user.weight
        val met = if (weightKg > 0.0) {
            validInput.kcalPerHour.toDouble() / weightKg
        } else {
            0.0
        }

        val activity = Activity(
            userId = user.id,
            name = validInput.name,
            iconName = selectedIcon,
            kcalPerHour = validInput.kcalPerHour,
            met = met,
            weightKg = weightKg,
            durationMins = validInput.durationMins,
            intensity = validInput.selectedIntensity.name,
            kcalBurned = estimatedKcalBurned,
            dateText = dateText
        )

        saveActivityUseCase(activity)
    }
}
