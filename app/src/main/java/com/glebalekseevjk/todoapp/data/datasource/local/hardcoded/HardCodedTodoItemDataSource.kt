package com.glebalekseevjk.todoapp.data.datasource.local.hardcoded

import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.domain.entity.TodoItem.Companion.exampleTodoItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Deprecated("Old class no longer in use")
class HardCodedTodoItemDataSource {
    private val _todoItemsStateFlow = MutableStateFlow(exampleTodoItems)
    val todoItemsFlow: Flow<List<TodoItem>> get() = _todoItemsStateFlow
    private val mutex = Mutex()

    @Throws(NoSuchElementException::class)
    suspend fun getTodoItem(id: String): TodoItem {
        delay(150)
        val todoItem = _todoItemsStateFlow.value.find { it.id == id }
        return todoItem ?: throw NoSuchElementException()
    }

    suspend fun insertOrReplaceTodoItem(todoItem: TodoItem) {
        delay(150)
        mutex.withLock {
            val list = _todoItemsStateFlow.value.toMutableList()
            val newTodoItem = try {
                val oldTodoItem = getTodoItem(todoItem.id)
                list.remove(oldTodoItem)
                todoItem
            } catch (e: NoSuchElementException) {
                todoItem.copy(id = UUID.randomUUID().toString())
            }
            list.add(newTodoItem)
            _todoItemsStateFlow.update {
                list.sortedBy { it.createdAt }
            }
        }
    }

    @Throws(NoSuchElementException::class)
    suspend fun deleteTodoItem(id: String) {
        delay(100)
        mutex.withLock {
            val oldTodoItem = getTodoItem(id)
            _todoItemsStateFlow.update {
                val list = _todoItemsStateFlow.value.toMutableList()
                list.remove(oldTodoItem)
                list.sortedBy { it.createdAt.time }
            }
        }
    }
}