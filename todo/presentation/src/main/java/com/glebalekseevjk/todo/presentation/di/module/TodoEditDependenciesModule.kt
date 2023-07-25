package com.glebalekseevjk.todo.presentation.di.module

import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.core.utils.di.DependenciesKey
import com.glebalekseevjk.todo.presentation.di.TodoComponent
import com.glebalekseevjk.todo.todoedit.presentation.di.TodoEditDependencies
import com.glebalekseevjk.todo.todolist.presentation.di.TodoListDependencies
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoEditDependenciesModule {
    @Binds
    @IntoMap
    @DependenciesKey(TodoEditDependencies::class)
    fun bindTodoEditDependencies(impl: TodoComponent): Dependencies
}