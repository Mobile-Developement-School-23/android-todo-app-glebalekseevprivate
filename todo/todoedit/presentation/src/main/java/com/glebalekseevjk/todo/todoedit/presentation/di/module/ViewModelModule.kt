package com.glebalekseevjk.todo.todoedit.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.core.utils.di.ViewModelFactory
import com.glebalekseevjk.core.utils.di.ViewModelKey
import com.glebalekseevjk.todo.todoedit.presentation.viewmodel.TodoItemViewModel
import com.glebalekseevjk.todo.todoedit.presentation.di.scope.TodoEditComponentScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @TodoEditComponentScope
    @Binds
    @[IntoMap ViewModelKey(TodoItemViewModel::class)]
    fun bindTodoItemViewModel(todoItemViewModel: TodoItemViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}