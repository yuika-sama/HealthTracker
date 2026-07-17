package com.yuika.healthtracker.service.pdf_exporter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.yuika.healthtracker.domain.usecase.main_use_cases.activity.GetActivitiesByDateRangeUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.food.GetFoodEntriesByDateRangeUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.CalculateUserStatsUseCase
import com.yuika.healthtracker.domain.usecase.main_use_cases.user.GetLatestUserUseCase
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import java.io.File
import java.io.FileOutputStream
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeeklyReportService @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val getLatestUserUseCase: GetLatestUserUseCase,
    private val getFoodEntriesByDateRangeUseCase: GetFoodEntriesByDateRangeUseCase,
    private val getActivitiesByDateRange: GetActivitiesByDateRangeUseCase,
    private val calculateUserStatsUseCase: CalculateUserStatsUseCase
)
{
    suspend fun exportCurrentWeek(): Uri
    {
        val start = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val end = minOf(LocalDate.now(), start.plusDays(6))
        return exportWeekOrNull(start, end)
            ?: throw IllegalArgumentException("Can't find user information")
    }

    suspend fun exportPreviousWeekOrNull(): Uri?
    {
        val start = LocalDate.now()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .minusWeeks(1)
        return exportWeekOrNull(start, start.plusDays(6))
    }

    private suspend fun exportWeekOrNull(
        start: LocalDate,
        end: LocalDate
    ): Uri?
    {
        val user = getLatestUserUseCase().firstOrNull() ?: return null
        val foods = getFoodEntriesByDateRangeUseCase(user.id, start.toString(), end.toString())
            .firstOrNull()
            .orEmpty()

        val activities = getActivitiesByDateRange(user.id, start.toString(), end.toString())
            .firstOrNull()
            .orEmpty()

        val foodsByDate = foods.groupBy { it.dateText }
        val activitiesByDate = activities.groupBy { it.dateText }
        val goal = calculateUserStatsUseCase(user).goalKcal

        val days = generateSequence(start) { it.plusDays(1) }
            .takeWhile { !it.isAfter(end) }
            .map { date ->
                val key = date.toString()
                WeeklyReportDay(
                    date = date,
                    intake = foodsByDate[key]?.sumOf { it.calories } ?: 0,
                    burned = activitiesByDate[key]?.sumOf { it.kcalBurned } ?: 0
                )
            }
            .toList()

        val report = WeeklyReportData(user.name, start, end, goal, days)
        val file = reportFile(start, end)
        writePdf(report, file)

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    private fun writePdf(
        report: WeeklyReportData,
        file: File
    )
    {
        val pdf = PdfDocument()

        try {
            val page = pdf.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create())
            val canvas = page.canvas
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLACK }

            var y = 48f

            fun text(value: String, size: Float = 14f, bold: Boolean = false) {
                paint.textSize = size
                paint.isFakeBoldText = bold
                canvas.drawText(value, 40f, y, paint)
                y += size + 10f
            }

            text("Weekly Health Report", 22f, true)
            text("${report.startDate} to ${report.endDate}")
            text("User: ${report.userName}")
            text("Goal: ${report.goalCalories} kcal/day")
            y += 10f

            text("Summary", 16f, true)
            text("Total intake: ${report.totalIntake} kcal")
            text("Total burned: ${report.totalBurned} kcal")
            text("Balance: ${report.totalBalance} kcal")
            text("Average intake: ${report.avgIntake} kcal/day")
            text("Average burned: ${report.avgBurned} kcal/day")
            text("Days meeting goal: ${report.daysMeetingGoal}/${report.days.size}")
            y += 12f

            text("Daily breakdown", 16f, true)
            report.days.forEach { day ->
                text("${day.date}: intake ${day.intake}, burned ${day.burned}, balance ${day.balance}", 12f)
            }

            pdf.finishPage(page)
            FileOutputStream(file).use { pdf.writeTo(it) }
        } finally {
            pdf.close()
        }
    }

    private fun reportFile(start: LocalDate, end: LocalDate): File
    {
        val root = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        return File(root, "weekly_reports/weekly_report_${start}_${end}.pdf").apply {
            parentFile?.mkdirs()
        }
    }

    companion object
    {
        fun from(context: Context): WeeklyReportService
        {
            return EntryPointAccessors.fromApplication(
                context.applicationContext,
                WeeklyReportEntryPoint::class.java
            ).weeklyReportService()
        }

        fun shareIntent(uri: Uri): Intent
        {
            return Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
    }
}