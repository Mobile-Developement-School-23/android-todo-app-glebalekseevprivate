package com.glebalekseevjk.todoapp.domain.repository

import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    fun getTodoItems(): Flow<Resource<List<TodoItem>>>
    fun getTodoItemOrNull(id: String): Resource<TodoItem?>
    suspend fun addTodoItem(todoItem: TodoItem): Resource<Unit>
    suspend fun updateTodoItem(todoItem: TodoItem): Resource<Unit>
    suspend fun changeDoneStatus(id: String): Resource<Unit>
    suspend fun setDoneStatus(id: String): Resource<Unit>
    suspend fun deleteTodoItem(id: String): Resource<Unit>
}