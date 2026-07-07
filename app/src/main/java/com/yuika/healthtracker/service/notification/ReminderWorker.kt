package com.yuika.healthtracker.service.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(
    context,
    workerParams
)
{
    override suspend fun doWork(): Result
    {
        return try {
            val notificationHelper = NotificationHelper(context)

            notificationHelper.showNotification(
                title = "This is Diary notification",
                message = "Don't forget updating your calories of your meal"
            )
            Result.success()
        } catch (_ : Exception){
            Result.retry()
        }
    }
}