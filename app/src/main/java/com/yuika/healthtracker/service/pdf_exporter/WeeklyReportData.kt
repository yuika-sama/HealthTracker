package com.yuika.healthtracker.service.pdf_exporter

import java.time.LocalDate

data class WeeklyReportData(
    val userName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val goalCalories: Int,
    val days: List<WeeklyReportDay>
){
    val totalIntake get() = days.sumOf{it.intake}
    val totalBurned get() = days.sumOf { it.burned }
    val totalBalance get() = totalIntake - totalBurned
    val avgIntake get() = totalIntake / days.size.coerceAtLeast(1)
    val avgBurned get() = totalBurned / days.size.coerceAtLeast(1)
    val daysMeetingGoal get() = days.count{
        it.intake > 0 && it.balance in (goalCalories - 300)..(goalCalories+200)
    }
}
