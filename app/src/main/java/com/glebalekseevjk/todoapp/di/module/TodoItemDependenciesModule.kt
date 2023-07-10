package com.glebalekseevjk.todoapp.di.module

import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.core.utils.di.DependenciesKey
import com.glebalekseevjk.feature.todoitem.di.TodoItemDependencies
import com.glebalekseevjk.todoapp.di.AppComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoItemDependenciesModule {
    @Binds
    @IntoMap
    @DependenciesKey(TodoItemDependencies::class)
    fun bindTodoItemDependencies(impl: AppComponent): Dependencies
}