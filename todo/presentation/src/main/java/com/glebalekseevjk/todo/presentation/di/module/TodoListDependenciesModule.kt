package com.glebalekseevjk.todo.presentation.di.module

import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.core.utils.di.DependenciesKey
import com.glebalekseevjk.todo.presentation.di.TodoComponent
import com.glebalekseevjk.todo.todolist.presentation.di.TodoListDependencies
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoListDependenciesModule {
    @Binds
    @IntoMap
    @DependenciesKey(TodoListDependencies::class)
    fun bindTodoListDependenciesModule(impl: TodoComponent): Dependencies
}