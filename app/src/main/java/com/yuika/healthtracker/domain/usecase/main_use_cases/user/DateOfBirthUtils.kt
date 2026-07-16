package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import java.time.LocalDate
import java.time.Period

private const val MIN_AGE = 10
private const val MAX_AGE = 120

fun parseDateOfBirth(value: String): LocalDate? =
    runCatching { LocalDate.parse(value.trim()) }.getOrNull()

fun calculateAgeFromDateOfBirth(dateOfBirth: String?): Int? {
    val birthDate = dateOfBirth?.let { parseDateOfBirth(it) } ?: return null
    return Period.between(birthDate, LocalDate.now()).years.takeIf { it in MIN_AGE..MAX_AGE }
}

fun validateDateOfBirth(value: String): Pair<String, Int> {
    val trimmedValue = value.trim()
    val age = calculateAgeFromDateOfBirth(trimmedValue)
        ?: throw IllegalArgumentException("Please enter a valid date of birth (yyyy-MM-dd, age 10-120)")
    return trimmedValue to age
}
