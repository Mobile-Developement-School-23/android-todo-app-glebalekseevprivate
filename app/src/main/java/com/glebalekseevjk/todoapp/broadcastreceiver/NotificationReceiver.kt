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
import com.glebalekseevjk.design.R
import com.glebalekseevjk.todo.domain.entity.TodoItem
import com.glebalekseevjk.todo.domain.entity.TodoItem.Companion.Importance.BASIC
import com.glebalekseevjk.todo.domain.entity.TodoItem.Companion.Importance.IMPORTANT
import com.glebalekseevjk.todo.domain.entity.TodoItem.Companion.Importance.LOW
import com.glebalekseevjk.todo.domain.repository.AlarmScheduler.Companion.NOTIFICATION_ID
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationRepository
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import com.glebalekseevjk.todo.todoedit.presentation.activity.TodoEditActivity
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var eventNotificationRepository: TodoEventNotificationRepository

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
            val eventNotification = try {
                eventNotificationRepository.getById(eventNotificationId)
            } catch (e: Exception) {
                return@launch
            }
            val todoItem = todoItemRepository.getByIdOrNull(eventNotification.todoItemId)
                ?: throw NoSuchElementException("Bad todoId from eventNotification")
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .configureIcon(todoItem, context)
                .setChannelId(CHANNEL_ID)
                .setContentTitle(context.getPreviewText(todoItem.text))
                .setContentText(todoItem.text)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(context.getAsideADayReceiverAction(todoItem))
                .setContentIntent(context.getTodoEditActivityPendingIntentWithEditFragment(todoItem))

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.resources.getString(R.string.deadline_notification_channel),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(
                Random(System.currentTimeMillis()).nextInt(),
                mBuilder.build()
            )
        }
    }

    private fun NotificationCompat.Builder.configureIcon(todoItem: TodoItem, context: Context) =
        apply {
            when (todoItem.importance) {
                LOW -> {
                    setSmallIcon(R.drawable.low)
                    color = context.resolveArgbAttr(R.attr.label_primary)
                }

                BASIC -> {
                    setSmallIcon(R.drawable.green_check)
                    color = context.resolveArgbAttr(R.attr.label_primary)
                }

                IMPORTANT -> {
                    setSmallIcon(R.drawable.important)
                    color = context.resolveArgbAttr(R.attr.color_red)
                }
            }
        }

    private fun Context.getPreviewText(text: String): String {
        val shortText = text.substring(
            0,
            minOf(text.length, 10)
        )
        val newValue =
            resources.getString(R.string.deadline_notification).let { String.format(it, shortText) }
        return if (shortText.length > 10) "$newValue..." else newValue
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
            R.drawable.blue_rounded_corners,
            resources.getString(R.string.postpone_for_a_day),
            pendingIntent
        ).build()
    }

    private fun Context.getTodoEditActivityPendingIntentWithEditFragment(todoItem: TodoItem): PendingIntent {
        val intent = Intent(this, TodoEditActivity::class.java)
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

fun Context.resolveArgbAttr(attr: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data.absoluteValue
}