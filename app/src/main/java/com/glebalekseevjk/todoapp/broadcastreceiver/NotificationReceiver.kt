package com.glebalekseevjk.todoapp.broadcastreceiver

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.TypedValue
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.glebalekseevjk.core.alarmscheduler.AlarmScheduler.Companion.NOTIFICATION_ID
import com.glebalekseevjk.design.R
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.domain.todoitem.entity.TodoItem.Companion.Importance.BASIC
import com.glebalekseevjk.domain.todoitem.entity.TodoItem.Companion.Importance.IMPORTANT
import com.glebalekseevjk.domain.todoitem.entity.TodoItem.Companion.Importance.LOW
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationRepository
import com.glebalekseevjk.domain.todoitem.repository.TodoItemRepository
import com.glebalekseevjk.todoapp.MainActivity
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.random.Random


fun Context.resolveArgbAttr(attr: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data.absoluteValue
}

class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var eventNotificationRepository: EventNotificationRepository

    @Inject
    lateinit var todoItemRepository: TodoItemRepository

    override fun onReceive(context: Context, intent: Intent) {
        context.appComponent.inject(this)
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val eventNotificationId = intent.getIntExtra(NOTIFICATION_ID, -1)
        if (eventNotificationId == -1) throw NoSuchElementException("Bad eventNotificationId from extra")

        CoroutineScope(Dispatchers.Default).launch {
            val eventNotification =
                eventNotificationRepository.getEventNotification(eventNotificationId)
            val todoItem = todoItemRepository.getTodoItemByIdOrNull(eventNotification.todoId)
                ?: throw NoSuchElementException("Bad todoId from eventNotification")
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .apply {
                    when (todoItem.importance) {
                        LOW -> {
                            setSmallIcon(R.drawable.low)
                            setColor(context.resolveArgbAttr(R.attr.label_primary))
                        }

                        BASIC -> {
                            setSmallIcon(R.drawable.green_check)
                            setColor(context.resolveArgbAttr(R.attr.label_primary))
                        }

                        IMPORTANT -> {
                            setSmallIcon(R.drawable.important)
                            setColor(context.resolveArgbAttr(R.attr.color_red))
                        }
                    }
                }
                .setChannelId(CHANNEL_ID)
                .setContentTitle(
                    "Дедлайн по делу: ${
                        todoItem.text.substring(
                            0,
                            minOf(todoItem.text.length, 10)
                        )
                    }...}"
                )
                .setContentText(todoItem.text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(context.getAsideADayReceiverAction(todoItem))
                .setContentIntent(context.getMainActivityPendingIntentWithEditFragment(todoItem))

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Канал уведомлений о дедлайне",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(Random(System.currentTimeMillis()).nextInt(), mBuilder.build())
        }
    }

    private fun Context.getAsideADayReceiverAction(todoItem: TodoItem): NotificationCompat.Action {
        val setAsideADayReceiverIntent = Intent(this, SetAsideADayReceiver::class.java).apply {
            putExtra(TODOITEM_ID, todoItem.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            todoItem.id.hashCode(),
            setAsideADayReceiverIntent,
            if (Build.VERSION.SDK_INT >= 30) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            com.glebalekseevjk.feature.todoitem.R.drawable.blue_rounded_corners,
            "Отложить на день",
            pendingIntent
        ).build()
    }

    private fun Context.getMainActivityPendingIntentWithEditFragment(todoItem: TodoItem): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(TODOITEM_ID, todoItem.id)
        return PendingIntent.getActivity(
            this,
            140,
            intent,
            if (Build.VERSION.SDK_INT >= 30) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    companion object {
        const val CHANNEL_ID = "com.glebalekseevjk.todoapp.event"
        const val TODOITEM_ID = "todoitem_id"
    }
}