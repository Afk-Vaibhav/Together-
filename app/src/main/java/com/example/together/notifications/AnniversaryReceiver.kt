package com.example.together.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.together.MainActivity

class AnniversaryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val channelId = "anniversary_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Anniversaries",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for relationship anniversaries"
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE
        )
        
        val title = if (action == AnniversaryScheduler.ACTION_YEARLY) {
            "Another year together 🤎"
        } else {
            "Another month together 🤎"
        }
        
        val text = if (action == AnniversaryScheduler.ACTION_YEARLY) {
            "Happy anniversary! 🧿"
        } else {
            "Happy monthly anniversary! 🧿"
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
            
        val notificationId = if (action == AnniversaryScheduler.ACTION_YEARLY) 2002 else 2001
        notificationManager.notify(notificationId, notification)
        
        AnniversaryScheduler(context).scheduleAll()
    }
}
