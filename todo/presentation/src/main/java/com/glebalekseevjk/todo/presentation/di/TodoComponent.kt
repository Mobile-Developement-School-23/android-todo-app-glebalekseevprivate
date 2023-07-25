package com.glebalekseevjk.todo.presentation.di

import androidx.navigation.NavController
import com.glebalekseevjk.todo.presentation.TodoFragment
import com.glebalekseevjk.todo.presentation.di.module.NavigationModule
import com.glebalekseevjk.todo.presentation.di.module.TodoEditDependenciesModule
import com.glebalekseevjk.todo.presentation.di.module.TodoListDependenciesModule
import com.glebalekseevjk.todo.presentation.di.scope.TodoComponentScope
import com.glebalekseevjk.todo.todoedit.presentation.di.TodoEditDependencies
import com.glebalekseevjk.todo.todolist.presentation.di.TodoListDependencies
import dagger.BindsInstance
import dagger.Component


@TodoComponentScope
@Component(
    dependencies = [
        TodoDependencies::class,
                   ],
    modules = [
        NavigationModule::class,
        TodoListDependenciesModule::class,
        TodoEditDependenciesModule::class
    ]
)
interface TodoComponent : TodoListDependencies, TodoEditDependencies {
    fun inject(activity: TodoFragment)

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: TodoDependencies,
        ): TodoComponent
    }
}