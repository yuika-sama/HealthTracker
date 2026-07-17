package com.yuika.healthtracker.service.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yuika.healthtracker.data.datastore.AppSettingsStore
import kotlinx.coroutines.flow.first

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(
    appContext,
    workerParams
)
{
    companion object {
        const val KEY_TYPE = "type"
        const val KEY_HOUR = "hour"

        const val TYPE_DAILY = "daily"
        const val TYPE_TEST = "test"
    }
    override suspend fun doWork(): Result
    {
        return runCatching {
            val settings = AppSettingsStore(applicationContext).settings.first()
            val type = inputData.getString(KEY_TYPE) ?: TYPE_DAILY

            val enabled = when (type) {
                TYPE_TEST -> settings.testNotificationEnabled
                TYPE_DAILY -> settings.notificationEnabled
                else -> false
            }
            if (!enabled) return Result.success()

            NotificationHelper(applicationContext).showNotification(
                title = "Diary reminder",
                message = "Don't forget to update your meal diary."
            )

            if (type == TYPE_TEST) {
                ReminderNotificationService(applicationContext).scheduleNextTestReminder()
            }

            Result.success()
        } .getOrElse {
            Result.retry()
        }
    }
}
