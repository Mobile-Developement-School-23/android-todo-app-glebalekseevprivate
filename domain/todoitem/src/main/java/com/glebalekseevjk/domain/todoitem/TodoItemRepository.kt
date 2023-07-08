package com.glebalekseevjk.domain.todoitem

import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
    suspend fun getTodoItemByIdOrNull(id: String): TodoItem?
    val todoItems: Flow<List<TodoItem>>
    suspend fun addTodoItem(todoItem: TodoItem)
    suspend fun changeDoneStatus(id: String)
    suspend fun setDoneStatus(id: String)
    suspend fun deleteTodoItem(id: String)
    suspend fun updateTodoItem(todoItem: TodoItem)
}