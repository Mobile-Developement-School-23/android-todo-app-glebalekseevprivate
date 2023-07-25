package com.glebalekseevjk.todo.todoedit.presentation.di

import com.glebalekseevjk.todo.todoedit.presentation.di.module.TodoItemFragmentModule
import com.glebalekseevjk.todo.todoedit.presentation.di.module.ViewModelModule
import com.glebalekseevjk.todo.todoedit.presentation.di.scope.TodoEditComponentScope
import com.glebalekseevjk.todo.todoedit.presentation.fragment.TodoItemFragment
import dagger.Component


@TodoEditComponentScope
@Component(
    dependencies = [TodoEditDependencies::class],
    modules = [TodoItemFragmentModule::class,ViewModelModule::class],
)
interface TodoEditComponent {
    fun inject(fragment: TodoItemFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: TodoEditDependencies): TodoEditComponent
    }
}