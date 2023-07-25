package com.glebalekseevjk.todo.todoedit.presentation.di

import android.content.Context
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.core.utils.di.OnBackPressed
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository

interface TodoEditDependencies : Dependencies {
    val todoItemRepository: TodoItemRepository
    val personalUseCase: PersonalUseCase
    @ApplicationContext
    fun getContext(): Context
}