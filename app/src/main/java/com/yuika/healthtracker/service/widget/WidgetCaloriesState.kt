package com.yuika.healthtracker.service.widget

import com.yuika.healthtracker.domain.model.calorieProgress
import kotlin.math.roundToInt

data class WidgetCaloriesState(
    val hasUser: Boolean,
    val targetCalories: Int,
    val eatenCalories: Int,
    val burnedCalories: Int,
    val balanceCalories: Int,
    val remainingCalories: Int
){
    val progress: Float
        get() = calorieProgress(eatenCalories, targetCalories)

    val progressPercent: Int get() = (progress * 100).roundToInt()

    companion object {
        fun empty() = WidgetCaloriesState(false, 0, 0, 0,0, 0)
    }
}
