package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.ui.core.model.IntensityLevel
import javax.inject.Inject

data class ValidActivityInput(
    val name: String,
    val kcalPerHour: Int,
    val durationMins: Int,
    val selectedIntensity: IntensityLevel
)

class ValidateActivityInputUseCase @Inject constructor()
{
    operator fun invoke(
        activityName: String,
        kcalPerHourStr: String,
        durationStr: String,
        selectedIntensity: IntensityLevel
    ): ValidActivityInput
    {
        val name = activityName.trim()
        if (name.isEmpty())
        {
            throw IllegalArgumentException("Please fill in your name")
        }

        val kcalPerHour = kcalPerHourStr.toIntOrNull()
        if (kcalPerHour == null || kcalPerHour <= 0)
        {
            throw IllegalArgumentException("Please fill in valid kcal/hour")
        }

        val duration = durationStr.toIntOrNull()
        if (duration == null || duration <= 0)
        {
            throw IllegalArgumentException("Please fill in valid practice duration")
        }

        return ValidActivityInput(
            name = name,
            kcalPerHour = kcalPerHour,
            durationMins = duration,
            selectedIntensity = selectedIntensity
        )
    }
}
