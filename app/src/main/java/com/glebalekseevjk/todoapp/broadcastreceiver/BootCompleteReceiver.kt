package com.glebalekseevjk.todoapp.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationRepository
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationSchedulerRepository
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BootCompleteReceiver : BroadcastReceiver() {
    @Inject
    lateinit var eventNotificationRepository: EventNotificationRepository

    @Inject
    lateinit var eventNotificationSchedulerRepository: EventNotificationSchedulerRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            context.appComponent.inject(this)
            CoroutineScope(Dispatchers.IO).launch {
                val result = eventNotificationRepository.getEventNotificationList()
                eventNotificationSchedulerRepository.scheduleNotificationEvent(result)
            }
        }
    }
}