package com.glebalekseevjk.todo.domain.repository

import com.glebalekseevjk.todo.domain.entity.TodoEventNotification

interface TodoEventNotificationSchedulerRepository {
    suspend fun schedule(item: TodoEventNotification)
    suspend fun scheduleList(list: List<TodoEventNotification>)
    suspend fun cancel(item: TodoEventNotification)
    suspend fun cancelList(list: List<TodoEventNotification>)
}