package com.glebalekseevjk.todo.todolist.presentation.di

import com.glebalekseevjk.todo.todolist.presentation.di.module.ViewModelModule
import com.glebalekseevjk.todo.todolist.presentation.di.scope.TodoListComponentScope
import com.glebalekseevjk.todo.todolist.presentation.fragment.TodoItemsFragment
import dagger.Component

@TodoListComponentScope
@Component(
    dependencies = [TodoListDependencies::class],
    modules = [ViewModelModule::class]
)
interface TodoListComponent {
    fun inject(fragment: TodoItemsFragment)
    fun todoListFragmentViewSubcomponentBuilder(): TodoListFragmentViewSubcomponent.Builder

    @Component.Factory
    interface Factory {
        fun create(dependencies: TodoListDependencies): TodoListComponent
    }
}