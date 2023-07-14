package com.glebalekseevjk.feature.todoitem.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.feature.todoitem.di.ViewModelFactory
import com.glebalekseevjk.feature.todoitem.di.ViewModelKey
import com.glebalekseevjk.feature.todoitem.di.scope.TodoItemsComponentScope
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.feature.todoitem.presentation.viewmodel.TodoItemsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @[IntoMap ViewModelKey(TodoItemViewModel::class)]
    fun bindTodoItemViewModel(todoItemViewModel: TodoItemViewModel): ViewModel

    @TodoItemsComponentScope
    @Binds
    @[IntoMap ViewModelKey(TodoItemsViewModel::class)]
    fun bindTodoItemsViewModel(todoItemsViewModel: TodoItemsViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}