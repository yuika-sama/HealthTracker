package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import com.yuika.healthtracker.domain.model.ActivityCatalog
import javax.inject.Inject

data class ValidActivityInput(
    val activity: ActivityCatalog,
    val durationMins: Int,
)

class ValidateActivityInputUseCase @Inject constructor()
{
    operator fun invoke(activity: ActivityCatalog?, durationStr: String): ValidActivityInput
    {
        val selected = activity ?: throw IllegalArgumentException("Please select an activity")
        val duration = durationStr.toIntOrNull()
        if (duration == null || duration <= 0)
        {
            throw IllegalArgumentException("Please fill in valid practice duration")
        }
        return ValidActivityInput(selected, duration)
    }
}
