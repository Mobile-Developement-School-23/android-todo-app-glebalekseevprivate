package com.glebalekseevjk.todo.todolist.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.core.utils.di.ViewModelFactory
import com.glebalekseevjk.core.utils.di.ViewModelKey
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.TodoItemsViewModel
import com.glebalekseevjk.todo.todolist.presentation.di.scope.TodoListComponentScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @TodoListComponentScope
    @Binds
    @[IntoMap ViewModelKey(TodoItemsViewModel::class)]
    fun bindTodoItemsViewModel(todoItemsViewModel: TodoItemsViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}