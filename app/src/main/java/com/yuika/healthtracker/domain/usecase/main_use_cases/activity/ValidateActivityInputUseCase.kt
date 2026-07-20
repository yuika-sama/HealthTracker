package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import android.content.Context
import com.yuika.healthtracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yuika.healthtracker.domain.model.ActivityCatalog
import javax.inject.Inject

data class ValidActivityInput(
    val activity: ActivityCatalog,
    val durationMins: Int,
)

class ValidateActivityInputUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(activity: ActivityCatalog?, durationStr: String): ValidActivityInput
    {
        val selected = activity ?: throw IllegalArgumentException(context.getString(R.string.error_select_activity))
        val duration = durationStr.toIntOrNull()
        if (duration == null || duration <= 0)
        {
            throw IllegalArgumentException(context.getString(R.string.error_valid_duration))
        }
        return ValidActivityInput(selected, duration)
    }
}
