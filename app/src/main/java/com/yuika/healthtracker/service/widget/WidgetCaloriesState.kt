package com.yuika.healthtracker.service.widget

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
        get() = if (targetCalories > 0){
            (balanceCalories / targetCalories.toFloat()).coerceIn(0f, 1f)
        } else 0f

    val progressPercent: Int get() = (progress * 100).roundToInt()

    companion object {
        fun empty() = WidgetCaloriesState(false, 0, 0, 0,0, 0)
    }
}