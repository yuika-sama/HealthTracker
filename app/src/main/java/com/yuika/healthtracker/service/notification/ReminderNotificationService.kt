package com.yuika.healthtracker.service.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderNotificationService @Inject constructor(
    @param:ApplicationContext private val context: Context
)
{
    private val workManager get() = WorkManager.getInstance(context)

    private fun dailyWorkName(hour: Int) = "diary_reminder_$hour"

    private companion object
    {
        val reminderHours = listOf(7, 12, 19)
        const val TEST_WORK_NAME = "diary_reminder_test_every_minute"
    }

    fun setDailyReminderEnabled(enabled: Boolean)
    {
        if (enabled) scheduleDailyReminders() else cancelDailyReminders()
    }

    fun setTestReminderEnabled(enabled: Boolean)
    {
        if (enabled) enqueueTestReminder(ExistingWorkPolicy.REPLACE) else cancelTestReminder()
    }

    fun scheduleNextTestReminder()
    {
        enqueueTestReminder(ExistingWorkPolicy.APPEND_OR_REPLACE)
    }

    private fun scheduleDailyReminders()
    {
        reminderHours.forEach { hour ->
            val request =
                PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS).setInitialDelay(
                    delayUntil(hour), TimeUnit.MILLISECONDS
                ).setInputData(
                    workDataOf(
                        ReminderWorker.KEY_TYPE to ReminderWorker.TYPE_DAILY,
                        ReminderWorker.KEY_HOUR to hour
                    )
                ).build()

            workManager.enqueueUniquePeriodicWork(
                dailyWorkName(hour), ExistingPeriodicWorkPolicy.UPDATE, request
            )
        }
    }

    private fun delayUntil(hour: Int): Long
    {
        val now = LocalDateTime.now()
        var target = now.withHour(hour).withMinute(0).withSecond(0).withNano(0)
        if (!target.isAfter(now)) target = target.plusDays(1)
        return Duration.between(now, target).toMillis()
    }

    private fun cancelDailyReminders()
    {
        reminderHours.forEach { hour ->
            workManager.cancelUniqueWork(dailyWorkName(hour))
        }
    }

    private fun cancelTestReminder()
    {
        workManager.cancelUniqueWork(TEST_WORK_NAME)
    }

    private fun enqueueTestReminder(policy: ExistingWorkPolicy)
    {
        val request =
            OneTimeWorkRequestBuilder<ReminderWorker>().setInitialDelay(1, TimeUnit.MINUTES)
                .setInputData(workDataOf(ReminderWorker.KEY_TYPE to ReminderWorker.TYPE_TEST))
                .build()

        workManager.enqueueUniqueWork(
            TEST_WORK_NAME,
            policy,
            request
        )
    }
}
