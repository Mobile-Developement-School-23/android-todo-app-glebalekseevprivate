package com.glebalekseevjk.domain.todoitem.repository

import com.glebalekseevjk.domain.todoitem.entity.EventNotification

interface EventNotificationSchedulerRepository {
    suspend fun scheduleNotificationEvent(eventNotification: EventNotification)
    suspend fun scheduleNotificationEvent(eventNotifications: List<EventNotification>)
    suspend fun cancelNotificationEvent(eventNotification: EventNotification)
    suspend fun cancelNotificationEvent(eventNotifications: List<EventNotification>)
}