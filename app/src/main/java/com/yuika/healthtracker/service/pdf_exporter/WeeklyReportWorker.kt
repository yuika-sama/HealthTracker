package com.yuika.healthtracker.service.pdf_exporter

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WeeklyReportWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams)
{
    override suspend fun doWork(): Result
    {
        return runCatching {
            WeeklyReportService.from(applicationContext).exportPreviousWeekOrNull()
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }
}