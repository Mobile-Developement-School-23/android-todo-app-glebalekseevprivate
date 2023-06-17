package com.glebalekseevjk.todoapp.domain.interactor

import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.domain.repository.TodoItemsRepository

class TodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    suspend fun add(todoItem: TodoItem) = todoItemsRepository.addTodoItem(todoItem)
    suspend fun update(todoItem: TodoItem) = todoItemsRepository.updateTodoItem(todoItem)
    fun getTodoItems() = todoItemsRepository.getTodoItems()
    fun getTodoItemOrNull(id: String) = todoItemsRepository.getTodoItemOrNull(id)
    suspend fun changeDoneStatus(id: String) = todoItemsRepository.changeDoneStatus(id)
    suspend fun setDoneStatus(id: String) = todoItemsRepository.setDoneStatus(id)
    suspend fun deleteTodoItem(id: String) = todoItemsRepository.deleteTodoItem(id)
}