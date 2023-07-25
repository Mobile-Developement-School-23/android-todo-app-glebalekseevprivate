package com.glebalekseevjk.todo.presentation.di

import android.content.Context
import com.glebalekseevjk.auth.domain.usecase.AuthUseCase
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationSchedulerManager
import com.glebalekseevjk.todo.todolist.presentation.di.NavigateToTodoEdit

interface TodoDependencies : Dependencies {
    @ApplicationContext
    fun getContext(): Context
    val todoItemRepository: TodoItemRepository
    val todoSynchronizationRepository: TodoSynchronizationRepository
    val todoSynchronizationSchedulerManager: TodoSynchronizationSchedulerManager
    val authUseCase: AuthUseCase
    val personalUseCase: PersonalUseCase

}