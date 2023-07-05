package com.glebalekseevjk.todoapp.ioc.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.todoapp.ioc.ViewModelFactory
import com.glebalekseevjk.todoapp.ioc.ViewModelKey
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @[IntoMap ViewModelKey(TodoItemViewModel::class)]
    fun bindTodoItemViewModel(todoItemViewModel: TodoItemViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(TodoItemsViewModel::class)]
    fun bindTodoItemsViewModel(todoItemsViewModel: TodoItemsViewModel): ViewModel

    @AppComponentScope
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}