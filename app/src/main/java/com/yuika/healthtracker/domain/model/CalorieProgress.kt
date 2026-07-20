package com.yuika.healthtracker.domain.model

fun calorieProgress(intakeCalories: Int, goalCalories: Int): Float =
    if (goalCalories > 0) (intakeCalories / goalCalories.toFloat()).coerceIn(0f, 1f) else 0f
