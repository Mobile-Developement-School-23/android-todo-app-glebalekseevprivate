package com.glebalekseevjk.todo.data.repository

import com.glebalekseevjk.core.utils.Mapper
import com.glebalekseevjk.todo.data.room.dao.TodoEventNotificationDao
import com.glebalekseevjk.todo.data.room.model.TodoEventNotificationDbModel
import com.glebalekseevjk.todo.domain.entity.TodoEventNotification
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 Этот класс представляет репозиторий уведомлений о событиях
 */

class TodoEventNotificationRepositoryImpl @Inject constructor(
    private val todoEventNotificationDao: TodoEventNotificationDao,
    private val mapper: Mapper<TodoEventNotification, TodoEventNotificationDbModel>
) : TodoEventNotificationRepository {
    override suspend fun add(item: TodoEventNotification) {
        withContext(Dispatchers.IO) {
            val result = mapper.mapItemToAnotherItem(item)
            todoEventNotificationDao.insert(result)
        }
    }

    override suspend fun removeById(id: Int) =
        withContext(Dispatchers.IO) {
            todoEventNotificationDao.delete(id)
        }

    override suspend fun getList(): List<TodoEventNotification> {
        val result = todoEventNotificationDao.getAll()
        return result.map { mapper.mapAnotherItemToItem(it) }
    }

    override suspend fun getById(id: Int): TodoEventNotification =
        withContext(Dispatchers.IO) {
            val result = todoEventNotificationDao.get(id)
            result ?: throw NoSuchElementException()
            return@withContext mapper.mapAnotherItemToItem(result)
        }
}