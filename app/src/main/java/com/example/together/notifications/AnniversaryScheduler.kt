package com.example.together.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.together.data.RelationshipPreferencesRepository
import com.example.together.domain.AnniversaryCalculator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

class AnniversaryScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val repository = RelationshipPreferencesRepository(context)

    companion object {
        const val ACTION_MONTHLY = "com.example.together.ACTION_MONTHLY_ANNIVERSARY"
        const val ACTION_YEARLY = "com.example.together.ACTION_YEARLY_ANNIVERSARY"
    }

    fun scheduleAll() {
        CoroutineScope(Dispatchers.IO).launch {
            val start = repository.relationshipStart.first() ?: return@launch
            val monthlyEnabled = repository.monthlyNotificationsEnabled.first()
            val yearlyEnabled = repository.yearlyNotificationsEnabled.first()

            val now = LocalDateTime.now()

            if (monthlyEnabled) {
                val nextMonthly = AnniversaryCalculator.getNextMonthlyAnniversary(start, now)
                schedule(ACTION_MONTHLY, nextMonthly, 1001)
            } else {
                cancel(ACTION_MONTHLY, 1001)
            }

            if (yearlyEnabled) {
                val nextYearly = AnniversaryCalculator.getNextYearlyAnniversary(start, now)
                schedule(ACTION_YEARLY, nextYearly, 1002)
            } else {
                cancel(ACTION_YEARLY, 1002)
            }
        }
    }

    private fun schedule(action: String, time: LocalDateTime, requestCode: Int) {
        val intent = Intent(context, AnniversaryReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: SecurityException) {
            Log.e("AnniversaryScheduler", "Permission denied for scheduling exact alarms", e)
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }

    private fun cancel(action: String, requestCode: Int) {
        val intent = Intent(context, AnniversaryReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
