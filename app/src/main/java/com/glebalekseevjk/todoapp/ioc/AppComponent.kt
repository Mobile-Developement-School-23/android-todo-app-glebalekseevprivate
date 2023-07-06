package com.glebalekseevjk.todoapp.ioc

import android.content.Context
import com.glebalekseevjk.todoapp.App
import com.glebalekseevjk.todoapp.ioc.module.LocalDataSourceModule
import com.glebalekseevjk.todoapp.ioc.module.RemoteDataSourceModule
import com.glebalekseevjk.todoapp.ioc.module.ViewModelModule
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import com.glebalekseevjk.todoapp.presentation.activity.MainActivity
import com.glebalekseevjk.todoapp.presentation.fragment.TodoItemFragment
import com.glebalekseevjk.todoapp.presentation.fragment.TodoItemsFragment
import com.glebalekseevjk.todoapp.worker.SynchronizeWorker
import dagger.BindsInstance
import dagger.Component

@AppComponentScope
@Component(modules = [ViewModelModule::class, LocalDataSourceModule::class, RemoteDataSourceModule::class])
interface AppComponent {
    fun createAuthActivitySubcomponent(): AuthActivitySubcomponent
    fun createTodoItemsFragmentSubcomponent(): TodoItemsFragmentSubcomponent
    fun createTodoItemFragmentSubcomponent(): TodoItemFragmentSubcomponent
    fun injectApp(application: App)
    fun injectMainActivity(mainActivity: MainActivity)
    fun injectTodoItemsFragment(todoItemsFragment: TodoItemsFragment)
    fun injectTodoItemFragment(todoItemFragment: TodoItemFragment)
    fun injectSynchronizeWorker(synchronizeWorker: SynchronizeWorker)

    @Component.Factory
    interface Builder {
        fun create(@BindsInstance context: Context): AppComponent
    }
}