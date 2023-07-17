package com.glebalekseevjk.core.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.glebalekseevjk.core.utils.di.ApplicationContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class AlarmScheduler @Inject constructor(@ApplicationContext val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(packageContext: Class<*>, id: Int, date: Date) {
        if (date > Calendar.getInstance().time) {
            val broadcastIntent = Intent(context, packageContext).apply {
                putExtra(NOTIFICATION_ID, id)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                broadcastIntent,
                if (Build.VERSION.SDK_INT >= 30) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                date.time,
                pendingIntent
            )
        }
    }

    fun cancel(packageContext: Class<*>, id: Int) {
        val broadcastIntent = Intent(context, packageContext)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            broadcastIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val NOTIFICATION_ID = "notification_id"
    }
}