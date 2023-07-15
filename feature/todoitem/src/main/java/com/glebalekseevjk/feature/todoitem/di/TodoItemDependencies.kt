package com.glebalekseevjk.feature.todoitem.di

import android.content.Context
import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.preferences.PersonalStorage
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.domain.sync.SynchronizationRepository
import com.glebalekseevjk.domain.sync.SynchronizationSchedulerManager
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationRepository
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationSchedulerRepository
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.domain.todoitem.repository.TodoItemRepository

interface TodoItemDependencies : Dependencies {
    val authRepository: AuthRepository
    val schedulerManager: SynchronizationSchedulerManager
    val synchronizationRepository: SynchronizationRepository
    val todoItemRepository: TodoItemRepository
    val eventNotificationRepository: EventNotificationRepository
    val eventNotificationSchedulerRepository: EventNotificationSchedulerRepository
    @ApplicationContext
    fun getContext(): Context
    val todoItemDao: TodoItemDao
    val todRemoveTodoItemDao: ToRemoveTodoItemDao
    val personalStorage: PersonalStorage
    val mapper: Mapper<TodoItem, TodoItemDbModel>
}