package com.glebalekseevjk.todo.data.repository

import com.glebalekseevjk.todo.data.alarmscheduler.AlarmSchedulerImpl
import com.glebalekseevjk.todo.domain.entity.TodoEventNotification
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationSchedulerRepository
import di.NotificationReceiverClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
Этот класс представляет репозиторий планировщика уведомлений о событиях
 */

class TodoEventNotificationSchedulerRepositoryImpl @Inject constructor(
    private val alarmScheduler: AlarmSchedulerImpl,
    @NotificationReceiverClass private val notificationReceiverClass: Class<*>
) : TodoEventNotificationSchedulerRepository {
    override suspend fun schedule(item: TodoEventNotification) =
        withContext(Dispatchers.IO) {
            alarmScheduler.schedule(
                notificationReceiverClass,
                item.id,
                item.date
            )
        }

    override suspend fun cancel(item: TodoEventNotification) =
        withContext(Dispatchers.IO) {
            alarmScheduler.cancel(notificationReceiverClass, item.id)
        }

    override suspend fun scheduleList(list: List<TodoEventNotification>) {
        list.forEach {
            schedule(it)
        }
    }

    override suspend fun cancelList(list: List<TodoEventNotification>) {
        list.forEach {
            cancel(it)
        }
    }
}