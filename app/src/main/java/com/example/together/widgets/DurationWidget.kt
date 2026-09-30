package com.example.together.widgets

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.together.MainActivity
import com.example.together.data.RelationshipPreferencesRepository
import com.example.together.domain.RelationshipDurationCalculator
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import androidx.glance.appwidget.cornerRadius

class DurationWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DurationWidget()
}

class DurationWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = RelationshipPreferencesRepository(context)
        val startTimestamp = repository.relationshipStart.first()
        
        val durationText = if (startTimestamp != null) {
            val now = LocalDateTime.now()
            val duration = RelationshipDurationCalculator.calculateCalendarDuration(startTimestamp, now)
            "${duration.years}y, ${duration.months}m, ${duration.days}d"
        } else {
            "Not set yet"
        }
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color(0x33FFFFFF))
                    .cornerRadius(24.dp)
                    .clickable(actionStartActivity(intent))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Together with!🤍",
                    style = TextStyle(color = androidx.glance.color.ColorProvider(day = Color.White, night = Color.White), fontSize = 12.sp)
                )
                Text(
                    text = durationText,
                    style = TextStyle(
                        color = androidx.glance.color.ColorProvider(day = Color.White, night = Color.White),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = GlanceModifier.padding(top = 8.dp)
                )
            }
        }
    }
}
