package com.glebalekseevjk.todo.data.repository

import com.glebalekseevjk.core.utils.Mapper
import com.glebalekseevjk.todo.data.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todo.data.room.dao.TodoItemDao
import com.glebalekseevjk.todo.data.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todo.data.room.model.TodoItemDbModel
import com.glebalekseevjk.todo.domain.entity.TodoEventNotification
import com.glebalekseevjk.todo.domain.entity.TodoItem
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationRepository
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationSchedulerRepository
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

/**
Этот класс представляет репозиторий элементов списка дел
 */
class TodoItemRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
    private val mapperTodoItemDbModel: Mapper<TodoItem, TodoItemDbModel>,
    private val eventNotificationRepository: TodoEventNotificationRepository,
    private val eventNotificationSchedulerRepository: TodoEventNotificationSchedulerRepository
) : TodoItemRepository {
    override val deletionNotification = Channel<String>()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            todoItemDao.getAllAsFlow().collectLatest {
                val eventNotificationList =
                    eventNotificationRepository.getList()
                eventNotificationSchedulerRepository.cancelList(
                    eventNotificationList
                )
                eventNotificationList.onEach {
                    eventNotificationRepository.removeById(
                        it.id
                    )
                }
                it
                    .filter { !it.isDone }
                    .filter { it.deadline != null }
                    .filter { it.deadline!! >= Calendar.getInstance().time }
                    .map {
                        TodoEventNotification(
                            todoItemId = it.id,
                            date = it.deadline!!,
                        )
                    }.onEach { eventNotificationRepository.add(it) }

                val newEventNotificationList =
                    eventNotificationRepository.getList()
                eventNotificationSchedulerRepository.scheduleList(
                    newEventNotificationList
                )
            }
        }
    }

    override suspend fun getByIdOrNull(id: String): TodoItem? {
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

    override suspend fun add(todoItem: TodoItem) {
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

    override suspend fun clear() {
        todoItemDao.deleteAll()
        toRemoveTodoItemDao.deleteAll()
    }

    private var lastRemovedTodoItem: TodoItemDbModel? = null
    override suspend fun delete(id: String) {
        withContext(Dispatchers.Default) {
            val item = todoItemDao.getById(id) ?: throw NoSuchElementException()
            todoItemDao.deleteById(id)
            toRemoveTodoItemDao.insertOrReplace(
                ToRemoveTodoItemDbModel(
                    id,
                    item.createdAt
                )
            )
            lastRemovedTodoItem = item
            deletionNotification.send(item.text)
        }
    }

    override suspend fun cancelDeletion() {
        withContext(Dispatchers.Default) {
            val lastRemovedItem = lastRemovedTodoItem
            lastRemovedItem ?: return@withContext
            toRemoveTodoItemDao.getById(lastRemovedItem.id)?.let {
                toRemoveTodoItemDao.deleteById(it.id)
            }
            todoItemDao.getById(lastRemovedItem.id)?.let { return@withContext }
            todoItemDao.insertOrReplace(lastRemovedItem)
        }
    }


    override suspend fun update(todoItem: TodoItem) {
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