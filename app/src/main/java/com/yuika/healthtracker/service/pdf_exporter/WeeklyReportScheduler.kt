package com.yuika.healthtracker.service.pdf_exporter

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeeklyReportScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context
)
{
    fun schedule(){
        val request = PeriodicWorkRequestBuilder<WeeklyReportWorker>(7, TimeUnit.DAYS)
            .setInitialDelay(delayUntilNextMonday7AM(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun delayUntilNextMonday7AM(): Long {
        val now = LocalDateTime.now()
        var target = now
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
            .withHour(7)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        if (!target.isAfter(now)) target = target.plusWeeks(1)
        return Duration.between(now, target).toMillis()
    }

    private companion object {
        const val WORK_NAME = "weekly_report_export"
    }
}