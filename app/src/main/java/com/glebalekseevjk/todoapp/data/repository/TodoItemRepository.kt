package com.glebalekseevjk.todoapp.data.repository

import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.TodoItemDbModel
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import com.glebalekseevjk.todoapp.utils.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@AppComponentScope
class TodoItemRepository @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
    private val mapperTodoItemDbModel: Mapper<TodoItem, TodoItemDbModel>,
) {
    suspend fun getTodoItemByIdOrNull(id: String): TodoItem? {
        return withContext(Dispatchers.Default) {
            return@withContext todoItemDao.getById(id)
                ?.let { mapperTodoItemDbModel.mapAnotherItemToItem(it) }
        }
    }

    val todoItems: Flow<List<TodoItem>> = todoItemDao
        .getAllAsFlow()
        .map { it.map { mapperTodoItemDbModel.mapAnotherItemToItem(it) } }
        .flowOn(Dispatchers.Default)

    suspend fun addTodoItem(todoItem: TodoItem) {
        return withContext(Dispatchers.Default) {
            val todoItemDbModel = mapperTodoItemDbModel.mapItemToAnotherItem(todoItem)
                .copy(id = UUID.randomUUID().toString())
            todoItemDao.insertOrReplace(todoItemDbModel)
        }
    }

    suspend fun changeDoneStatus(id: String) {
        return withContext(Dispatchers.Default) {
            val item = todoItemDao.getById(id) ?: throw NoSuchElementException()
            todoItemDao.insertOrReplace(
                item.copy(
                    isDone = !item.isDone,
                    changedAt = Calendar.getInstance().time
                )
            )
        }
    }

    suspend fun setDoneStatus(id: String) {
        return withContext(Dispatchers.Default) {
            val item = todoItemDao.getById(id) ?: throw NoSuchElementException()
            todoItemDao.insertOrReplace(
                item.copy(
                    isDone = true,
                    changedAt = Calendar.getInstance().time
                )
            )
        }
    }

    suspend fun deleteTodoItem(id: String) {
        withContext(Dispatchers.Default) {
            val item = todoItemDao.getById(id) ?: throw NoSuchElementException()
            todoItemDao.deleteById(id)
            toRemoveTodoItemDao.insertOrReplace(ToRemoveTodoItemDbModel(id, item.createdAt))
        }
    }

    suspend fun updateTodoItem(todoItem: TodoItem) {
        withContext(Dispatchers.Default) {
            val todoItemDbModel = mapperTodoItemDbModel.mapItemToAnotherItem(todoItem)
            todoItemDao.insertOrReplace(
                todoItemDbModel.copy(
                    changedAt = Calendar.getInstance().time
                )
            )
        }
    }
}