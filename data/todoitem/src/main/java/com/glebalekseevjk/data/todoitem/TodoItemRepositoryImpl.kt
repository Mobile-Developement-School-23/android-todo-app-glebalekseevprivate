package com.glebalekseevjk.data.todoitem

import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todoapp.domain.entity.entity.TodoItem
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.domain.todoitem.TodoItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
    private val mapperTodoItemDbModel: Mapper<TodoItem, TodoItemDbModel>,
) : TodoItemRepository {
    override suspend fun getTodoItemByIdOrNull(id: String): TodoItem? {
        return withContext(Dispatchers.Default) {
            return@withContext todoItemDao.getById(id)
                ?.let { mapperTodoItemDbModel.mapAnotherItemToItem(it) }
        }
    }

    override val todoItems: Flow<List<TodoItem>> = todoItemDao
        .getAllAsFlow()
        .map { it.map { mapperTodoItemDbModel.mapAnotherItemToItem(it) } }
        .map { it.sortedBy { it.createdAt.time } }
        .flowOn(Dispatchers.Default)

    override suspend fun addTodoItem(todoItem: TodoItem) {
        return withContext(Dispatchers.Default) {
            val todoItemDbModel = mapperTodoItemDbModel.mapItemToAnotherItem(todoItem)
                .copy(id = UUID.randomUUID().toString())
            todoItemDao.insertOrReplace(todoItemDbModel)
        }
    }

    override suspend fun changeDoneStatus(id: String) {
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

    override suspend fun setDoneStatus(id: String) {
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

    override suspend fun deleteTodoItem(id: String) {
        withContext(Dispatchers.Default) {
            val item = todoItemDao.getById(id) ?: throw NoSuchElementException()
            todoItemDao.deleteById(id)
            toRemoveTodoItemDao.insertOrReplace(
                ToRemoveTodoItemDbModel(
                    id,
                    item.createdAt
                )
            )
        }
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
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