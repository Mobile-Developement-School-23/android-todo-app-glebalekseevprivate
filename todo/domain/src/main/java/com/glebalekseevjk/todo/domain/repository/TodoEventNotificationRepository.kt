package com.glebalekseevjk.todo.domain.repository

import com.glebalekseevjk.todo.domain.entity.TodoEventNotification

interface TodoEventNotificationRepository {
    suspend fun add(item: TodoEventNotification)
    suspend fun removeById(id: Int)
    suspend fun getList(): List<TodoEventNotification>
    suspend fun getById(id: Int): TodoEventNotification
}