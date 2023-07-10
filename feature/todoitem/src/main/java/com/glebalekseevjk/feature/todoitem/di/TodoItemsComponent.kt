package com.glebalekseevjk.feature.todoitem.di

import com.glebalekseevjk.feature.todoitem.di.module.RepositoryModule
import com.glebalekseevjk.feature.todoitem.di.module.ViewModelModule
import com.glebalekseevjk.feature.todoitem.di.scope.TodoItemsComponentScope
import com.glebalekseevjk.feature.todoitem.presentation.fragment.TodoItemsFragment
import dagger.Component

@TodoItemsComponentScope
@Component(
    dependencies = [TodoItemDependencies::class],
    modules = [ViewModelModule::class, RepositoryModule::class]
)
interface TodoItemsComponent {
    fun inject(fragment: TodoItemsFragment)
    fun todoItemsFragmentViewSubcomponentBuilder(): TodoItemsFragmentViewSubcomponent.Builder
    fun createTodoItemFragmentSubcomponent(): TodoItemFragmentSubcomponent

    @Component.Factory
    interface Factory {
        fun create(dependencies: TodoItemDependencies): TodoItemsComponent
    }
}