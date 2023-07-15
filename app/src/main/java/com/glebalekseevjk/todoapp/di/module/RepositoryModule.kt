package com.glebalekseevjk.todoapp.di.module

import com.glebalekseevjk.data.auth.AuthRepositoryImpl
import com.glebalekseevjk.data.sync.SynchronizationRepositoryImpl
import com.glebalekseevjk.data.todoitem.repository.EventNotificationRepositoryImpl
import com.glebalekseevjk.data.todoitem.repository.EventNotificationSchedulerRepositoryImpl
import com.glebalekseevjk.data.todoitem.repository.TodoItemRepositoryImpl
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.domain.sync.SynchronizationRepository
import com.glebalekseevjk.domain.sync.SynchronizationSchedulerManager
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationRepository
import com.glebalekseevjk.domain.todoitem.repository.EventNotificationSchedulerRepository
import com.glebalekseevjk.domain.todoitem.repository.TodoItemRepository
import com.glebalekseevjk.feature.todoitem.di.scope.TodoItemsComponentScope
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
import com.glebalekseevjk.todoapp.worker.SynchronizationSchedulerManagerImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppComponentScope
    @Binds
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @AppComponentScope
    @Binds
    fun bindSynchronizationRepository(
        synchronizationRepositoryImpl: SynchronizationRepositoryImpl
    ): SynchronizationRepository
    @AppComponentScope
    @Binds
    fun bindEventNotificationRepository(
       eventNotificationRepositoryImpl: EventNotificationRepositoryImpl
    ): EventNotificationRepository

    @AppComponentScope
    @Binds
    fun bindEventNotificationSchedulerRepository(
        eventNotificationSchedulerRepositoryImpl: EventNotificationSchedulerRepositoryImpl
    ): EventNotificationSchedulerRepository

    @AppComponentScope
    @Binds
    fun bindTodoItemRepository(todoItemRepositoryImpl: TodoItemRepositoryImpl): TodoItemRepository

    @AppComponentScope
    @Binds
    fun bindSynchronizationSchedulerManager(
        synchronizationSchedulerManagerImpl: SynchronizationSchedulerManagerImpl
    ): SynchronizationSchedulerManager
}