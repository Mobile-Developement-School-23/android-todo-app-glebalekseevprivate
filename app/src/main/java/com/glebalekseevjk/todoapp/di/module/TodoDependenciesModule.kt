package com.glebalekseevjk.todoapp.di.module

import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.core.utils.di.DependenciesKey
import com.glebalekseevjk.todo.presentation.di.TodoDependencies
import com.glebalekseevjk.todoapp.di.AppComponent
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoDependenciesModule {
    @Binds
    @IntoMap
    @DependenciesKey(TodoDependencies::class)
    fun bindTodoDependencies(impl: AppComponent): Dependencies
}