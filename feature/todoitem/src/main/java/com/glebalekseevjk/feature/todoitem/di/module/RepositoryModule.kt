package com.glebalekseevjk.feature.todoitem.di.module

import com.glebalekseevjk.data.todoitem.TodoItemRepositoryImpl
import com.glebalekseevjk.domain.todoitem.TodoItemRepository
import com.glebalekseevjk.feature.todoitem.di.scope.TodoItemsComponentScope
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @TodoItemsComponentScope
    @Binds
    fun bindTodoItemRepository(todoItemRepositoryImpl: TodoItemRepositoryImpl): TodoItemRepository
}