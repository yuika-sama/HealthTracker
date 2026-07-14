package com.yuika.healthtracker.domain.usecase.main_use_cases.dashboard

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FormatDashboardDateUseCase @Inject constructor() {
    operator fun invoke(date: LocalDate = LocalDate.now()): Pair<String, String> {
        val dbDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val displayFormatter = DateTimeFormatter.ofPattern("MMM, dd")

        val dbDateText = date.format(dbDateFormat)
        val displayDateTime = "Today, ${date.format(displayFormatter)}"

        return Pair(dbDateText, displayDateTime)
    }
}
