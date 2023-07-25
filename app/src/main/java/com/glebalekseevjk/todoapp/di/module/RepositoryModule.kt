package com.glebalekseevjk.todoapp.di.module

import com.glebalekseevjk.auth.data.repository.AuthRepositoryImpl
import com.glebalekseevjk.auth.domain.repository.AuthRepository
import com.glebalekseevjk.todo.data.repository.TodoEventNotificationRepositoryImpl
import com.glebalekseevjk.todo.data.repository.TodoEventNotificationSchedulerRepositoryImpl
import com.glebalekseevjk.todo.data.repository.TodoItemRepositoryImpl
import com.glebalekseevjk.todo.data.repository.TodoSynchronizationRepositoryImpl
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationRepository
import com.glebalekseevjk.todo.domain.repository.TodoEventNotificationSchedulerRepository
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationSchedulerManager
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
import com.glebalekseevjk.todoapp.worker.TodoSynchronizationSchedulerManagerImpl
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
        synchronizationRepositoryImpl: TodoSynchronizationRepositoryImpl
    ): TodoSynchronizationRepository

    @AppComponentScope
    @Binds
    fun bindEventNotificationRepository(
        eventNotificationRepositoryImpl: TodoEventNotificationRepositoryImpl
    ): TodoEventNotificationRepository

    @AppComponentScope
    @Binds
    fun bindEventNotificationSchedulerRepository(
        eventNotificationSchedulerRepositoryImpl: TodoEventNotificationSchedulerRepositoryImpl
    ): TodoEventNotificationSchedulerRepository

    @AppComponentScope
    @Binds
    fun bindTodoItemRepository(todoItemRepositoryImpl: TodoItemRepositoryImpl): TodoItemRepository

    @AppComponentScope
    @Binds
    fun bindSynchronizationSchedulerManager(
        todoSynchronizationSchedulerManagerImpl: TodoSynchronizationSchedulerManagerImpl
    ): TodoSynchronizationSchedulerManager
}