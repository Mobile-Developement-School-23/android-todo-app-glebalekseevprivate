package com.glebalekseevjk.todo.domain.repository

import com.glebalekseevjk.todo.domain.entity.TodoItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
    suspend fun getByIdOrNull(id: String): TodoItem?
    suspend fun add(todoItem: TodoItem)
    suspend fun delete(id: String)
    suspend fun cancelDeletion()
    suspend fun update(todoItem: TodoItem)
    suspend fun changeDoneStatus(id: String)
    suspend fun setDoneStatus(id: String)
    suspend fun clear()

    val todoItems: Flow<List<TodoItem>>
    val deletionNotification: Channel<String>
}