package com.glebalekseevjk.todo.todolist.presentation.di

import android.content.Context
import androidx.navigation.NavController
import com.glebalekseevjk.auth.domain.usecase.AuthUseCase
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.core.utils.di.OnBackPressed
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationSchedulerManager
import javax.inject.Qualifier

interface TodoListDependencies : Dependencies {
    @NavigateToTodoEdit
    fun navigateToTodoEdit(): (String, NavController) -> Unit
    val todoItemRepository: TodoItemRepository
    val authUseCase: AuthUseCase
    val personalUseCase: PersonalUseCase
    val todoSynchronizationRepository: TodoSynchronizationRepository
    val todoSynchronizationSchedulerManager: TodoSynchronizationSchedulerManager
    @ApplicationContext
    fun getContext(): Context
}

@Qualifier
annotation class NavigateToTodoEdit