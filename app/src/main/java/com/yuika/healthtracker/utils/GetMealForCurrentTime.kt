package com.yuika.healthtracker.utils

import com.yuika.healthtracker.ui.features.main_features.add_meal.AddMealIntent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun getMealIntentForCurrentTime(): String {
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val currentHour = LocalTime.now().hour

    val mealType = when (currentHour) {
        in 5..9 -> "Breakfast"
        in 10..15 -> "Lunch"
        in 16..20 -> "Dinner"
        else -> "Snack"
    }

    return mealType
}