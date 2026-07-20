package com.yuika.healthtracker.domain.usecase.main_use_cases.activity

import android.content.Context
import com.yuika.healthtracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yuika.healthtracker.domain.model.Activity
import com.yuika.healthtracker.domain.model.ActivityCatalog
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.math.roundToInt

class ValidateAndSaveActivityUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val saveActivityUseCase: SaveActivityUseCase,
    private val validateActivityInputUseCase: ValidateActivityInputUseCase
) {
    suspend operator fun invoke(
        selectedActivity: ActivityCatalog?,
        durationStr: String,
        dateText: String
    ) {
        val input = validateActivityInputUseCase(selectedActivity, durationStr)
        val user = getLatestUserUseCase().firstOrNull()
            ?: throw IllegalArgumentException(context.getString(R.string.error_cannot_find_user_info))

        val kcal = (input.activity.met * user.weight * (input.durationMins / 60.0)).roundToInt()

        saveActivityUseCase(
            Activity(
                userId = user.id,
                activityCatalogId = input.activity.id,
                name = input.activity.name,
                iconName = input.activity.iconName,
                kcalPerHour = (input.activity.met * user.weight).roundToInt(),
                met = input.activity.met,
                weightKg = user.weight,
                durationMins = input.durationMins,
                intensity = input.activity.intensity,
                kcalBurned = kcal,
                dateText = dateText
            )
        )
    }
}
