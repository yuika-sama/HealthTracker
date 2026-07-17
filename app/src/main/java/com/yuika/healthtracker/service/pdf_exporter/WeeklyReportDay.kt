package com.yuika.healthtracker.service.pdf_exporter

import java.time.LocalDate

data class WeeklyReportDay(val date: LocalDate, val intake: Int, val burned: Int){
    val balance: Int get() = intake - burned
}
