package com.glebalekseevjk.data.todoitem.repository

import com.glebalekseevjk.core.alarmscheduler.AlarmScheduler
import com.glebalekseevjk.domain.todoitem.entity.EventNotification
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationSchedulerRepository
import di.NotificationReceiverClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventNotificationSchedulerRepositoryImpl @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    @NotificationReceiverClass private val notificationReceiverClass: Class<*>
) : EventNotificationSchedulerRepository {
    override suspend fun scheduleNotificationEvent(eventNotification: EventNotification) =
        withContext(Dispatchers.IO) {
            alarmScheduler.schedule(
                notificationReceiverClass,
                eventNotification.id,
                eventNotification.date
            )
        }

    override suspend fun cancelNotificationEvent(eventNotification: EventNotification) =
        withContext(Dispatchers.IO) {
            alarmScheduler.cancel(notificationReceiverClass, eventNotification.id)
        }

    override suspend fun scheduleNotificationEvent(eventNotifications: List<EventNotification>) {
        eventNotifications.forEach {
            scheduleNotificationEvent(it)
        }
    }

    override suspend fun cancelNotificationEvent(eventNotifications: List<EventNotification>) {
        eventNotifications.forEach {
            cancelNotificationEvent(it)
        }
    }
}