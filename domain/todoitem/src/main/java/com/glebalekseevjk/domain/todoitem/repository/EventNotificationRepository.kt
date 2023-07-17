package com.glebalekseevjk.domain.todoitem.repository

import com.glebalekseevjk.domain.todoitem.entity.EventNotification
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface EventNotificationRepository {
    suspend fun addEventNotification(eventNotification: EventNotification)
    suspend fun removeEventNotification(eventNotificationId: Int)
    suspend fun getEventNotificationList(): List<EventNotification>
    suspend fun getEventNotification(eventNotificationId: Int): EventNotification
}

