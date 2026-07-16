package com.yuika.healthtracker.domain.usecase.main_use_cases.user

import java.time.LocalDate
import java.time.Period

private const val MIN_YEARS_OLD = 10
private const val MAX_YEARS_OLD = 120

fun parseDateOfBirth(value: String): LocalDate? =
    runCatching { LocalDate.parse(value.trim()) }.getOrNull()

fun calculateYearsOldFromDateOfBirth(dateOfBirth: String?): Int? {
    val birthDate = dateOfBirth?.let { parseDateOfBirth(it) } ?: return null
    return Period.between(birthDate, LocalDate.now()).years.takeIf { it in MIN_YEARS_OLD..MAX_YEARS_OLD }
}

fun validateDateOfBirth(value: String): Pair<String, Int> {
    val trimmedValue = value.trim()
    val yearsOld = calculateYearsOldFromDateOfBirth(trimmedValue)
        ?: throw IllegalArgumentException("Please enter a valid date of birth (yyyy-MM-dd, 10-120 years old)")
    return trimmedValue to yearsOld
}
