package com.example.together.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import com.example.together.MainActivity
import com.example.together.R
import com.example.together.data.RelationshipPreferencesRepository
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.LocalDateTime

class ElapsedTimeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ElapsedTimeWidget()
}

class ElapsedTimeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = RelationshipPreferencesRepository(context)
        val startTimestamp = repository.relationshipStart.first()
        
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_elapsed_time)
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

        if (startTimestamp != null) {
            val now = LocalDateTime.now()
            val duration = Duration.between(startTimestamp, now)
            val totalMillis = duration.toMillis()
            
            val days = totalMillis / (24 * 3600 * 1000)
            val remainingMillis = totalMillis % (24 * 3600 * 1000)
            
            remoteViews.setTextViewText(R.id.tv_days, days.toString())
            
            val base = SystemClock.elapsedRealtime() - remainingMillis
            // Use "%s" to display only hours, minutes, and seconds
            remoteViews.setChronometer(R.id.chronometer, base, "%s", true)
        }
        
        provideContent {
            AndroidRemoteViews(remoteViews = remoteViews)
        }
    }
}
