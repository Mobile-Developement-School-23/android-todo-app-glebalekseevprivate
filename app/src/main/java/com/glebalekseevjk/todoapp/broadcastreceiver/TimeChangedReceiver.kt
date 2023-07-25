package com.glebalekseevjk.todoapp.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationRepository
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationSchedulerRepository
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeChangedReceiver : BroadcastReceiver() {
    @Inject
    lateinit var todoEventNotificationRepository: TodoEventNotificationRepository

    @Inject
    lateinit var todoEventNotificationSchedulerRepository: TodoEventNotificationSchedulerRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.TIME_SET") {
            context.appComponent.inject(this)
            CoroutineScope(Dispatchers.IO).launch {
                val result = todoEventNotificationRepository.getList()
                todoEventNotificationSchedulerRepository.scheduleList(result)
            }
        }
    }
}