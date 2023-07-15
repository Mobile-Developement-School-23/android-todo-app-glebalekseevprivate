package com.glebalekseevjk.data.todoitem.repository

import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.room.dao.EventNotificationDao
import com.glebalekseevjk.core.room.model.EventNotificationDbModel
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationRepository
import com.glebalekseevjk.domain.todoitem.entity.EventNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventNotificationRepositoryImpl @Inject constructor(
    private val eventNotificationDao: EventNotificationDao,
    private val mapper: Mapper<EventNotification, EventNotificationDbModel>
) : EventNotificationRepository {
    override suspend fun addEventNotification(eventNotification: EventNotification) {
        withContext(Dispatchers.IO) {
            val item = mapper.mapItemToAnotherItem(eventNotification)
            eventNotificationDao.insert(item)
        }
    }

    override suspend fun removeEventNotification(eventNotificationId: Int) =
        withContext(Dispatchers.IO) {
            try {
                eventNotificationDao.delete(eventNotificationId)
            } catch (e: Exception) {
                throw NoSuchElementException()
            }
        }

    override suspend fun getEventNotification(eventNotificationId: Int): EventNotification =
        withContext(Dispatchers.IO) {
            val result = eventNotificationDao.get(eventNotificationId)
            result ?: throw NoSuchElementException()
            return@withContext mapper.mapAnotherItemToItem(result)
        }

    override suspend fun getEventNotificationList(): List<EventNotification> {
        val result = eventNotificationDao.getAll()
        return result.map { mapper.mapAnotherItemToItem(it) }
    }
}