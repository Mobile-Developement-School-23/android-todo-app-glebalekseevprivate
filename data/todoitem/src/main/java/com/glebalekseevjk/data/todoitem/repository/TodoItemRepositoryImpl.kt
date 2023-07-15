package com.glebalekseevjk.data.todoitem.repository

import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.domain.todoitem.entity.EventNotification
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationRepository
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationSchedulerRepository
import com.glebalekseevjk.domain.todoitem.repository.TodoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
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
Ответственность класса TodoItemRepositoryImpl:
Этот класс является реализацией интерфейса TodoItemRepository
и отвечает за взаимодействие с базой данных для операций CRUD
(создание, чтение, обновление и удаление) элементов списка задач.
Он содержит методы для получения задачи по идентификатору, добавления новой задачи,
изменения статуса выполнения, удаления задачи и обновления информации о задаче в базе данных.
 */
@OptIn(InternalCoroutinesApi::class)
class TodoItemRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
    private val mapperTodoItemDbModel: Mapper<TodoItem, TodoItemDbModel>,
    private val eventNotificationRepository: EventNotificationRepository,
    private val eventNotificationSchedulerRepository: EventNotificationSchedulerRepository
) : TodoItemRepository {
    override val deletionNotification = Channel<String>()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            todoItemDao.getAllAsFlow().collectLatest {
                val eventNotificationList =
                    eventNotificationRepository.getEventNotificationList()
                eventNotificationSchedulerRepository.cancelNotificationEvent(
                    eventNotificationList
                )
                eventNotificationList.onEach {
                    eventNotificationRepository.removeEventNotification(
                        it.id
                    )
                }
                it
                    .filter { !it.isDone }
                    .filter { it.deadline != null }
                    .filter { it.deadline!! >= Calendar.getInstance().time }
                    .map {
                        EventNotification(
                            todoId = it.id,
                            date = it.deadline!!,
                        )
                    }.onEach { eventNotificationRepository.addEventNotification(it) }

                val newEventNotificationList =
                    eventNotificationRepository.getEventNotificationList()
                eventNotificationSchedulerRepository.scheduleNotificationEvent(
                    newEventNotificationList
                )
            }
        }
    }

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

    private var lastRemovedTodoItem: TodoItemDbModel? = null
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
            lastRemovedTodoItem = item
            deletionNotification.send(item.text)
        }
    }

    override suspend fun cancelDeletionTodoItem() {
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