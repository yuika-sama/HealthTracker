package com.yuika.healthtracker.utils

import java.time.LocalTime

fun getMealIntentForCurrentTime(): String {
    val currentHour = LocalTime.now().hour

    val mealType = when (currentHour) {
        in 5..9 -> "Breakfast"
        in 10..15 -> "Lunch"
        in 16..20 -> "Dinner"
        else -> "Snack"
    }

    return mealType
}
