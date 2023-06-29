package com.glebalekseevjk.todoapp.data.repository

import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.domain.repository.TodoItemsRepository
import com.glebalekseevjk.todoapp.utils.Resource
import com.glebalekseevjk.todoapp.utils.checkFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

private val todoItems = MutableStateFlow(TodoItem.exampleTodoItems)

@Deprecated("Old class no longer in use")
class TodoItemsRepositoryImpl : TodoItemsRepository {
    override fun getTodoItems(): Flow<Resource<List<TodoItem>>> =
        todoItems.transform { emit(Resource.Success(it)) }

    override fun getTodoItemOrNull(id: String): Resource<TodoItem?> =
        Resource.Success(todoItems.value.find { it.id == id })

    override suspend fun addTodoItem(todoItem: TodoItem): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val maxId = todoItems.value.maxOf { it.id.toInt() }
            todoItems.update {
                val currentList = todoItems.value.toList().toMutableList()
                currentList.add(todoItem.copy(id = (maxId + 1).toString()))
                currentList
            }
            return@withContext Resource.Success(Unit)
        }

    override suspend fun changeDoneStatus(id: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val todoItem = getTodoItemOrNull(id).checkFailure()
            todoItem ?: return@withContext Resource.Failure(NoSuchElementException())
            return@withContext updateTodoItem(todoItem.copy(isDone = !todoItem.isDone))
        }

    override suspend fun setDoneStatus(id: String): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val todoItem = getTodoItemOrNull(id).checkFailure()
            todoItem ?: return@withContext Resource.Failure(NoSuchElementException())
            return@withContext updateTodoItem(todoItem.copy(isDone = true))
        }

    override suspend fun deleteTodoItem(id: String): Resource<Unit> = withContext(Dispatchers.IO) {
        val oldTodoItem = getTodoItemOrNull(id).checkFailure()
        oldTodoItem ?: return@withContext Resource.Failure(NoSuchElementException())
        todoItems.update {
            val currentList = todoItems.value.toList().toMutableList()
            currentList.remove(oldTodoItem)
            currentList.sortedBy { it.id.toInt() }.toMutableList()
        }
        return@withContext Resource.Success(Unit)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val oldTodoItem = getTodoItemOrNull(todoItem.id).checkFailure()
            oldTodoItem ?: return@withContext Resource.Failure(NoSuchElementException())
            todoItems.update {
                val currentList = todoItems.value.toList().toMutableList()
                currentList.remove(oldTodoItem)
                currentList.add(todoItem)
                currentList.sortedBy { it.id.toInt() }.toMutableList()
            }
            return@withContext Resource.Success(Unit)
        }
}