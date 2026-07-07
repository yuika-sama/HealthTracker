package com.yuika.healthtracker.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yuika.healthtracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
)
{
    private val channelId = "diary_reminder_channel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Diary Reminder Channel"
            val descriptionText = "Channel for Diary Reminder Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String){
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}