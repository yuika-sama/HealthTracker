package com.yuika.healthtracker.domain.usecase.main_use_cases.diary

import java.time.LocalDate
import javax.inject.Inject

class ValidateDiaryLogicUseCase @Inject constructor() {
    
    fun validateMealType(mealType: String) {
        val validMeals = listOf("Breakfast", "Lunch", "Dinner", "Snack")
        if (mealType !in validMeals) {
            throw IllegalArgumentException("Meal type is not valid")
        }
    }

    fun validateDate(newDate: LocalDate) {
        val today = LocalDate.now()
        if (newDate.isAfter(today)) {
            throw IllegalArgumentException("Can't write a log a day in future")
        }
    }
}
