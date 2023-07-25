package com.glebalekseevjk.todoapp.di

import android.content.Context
import com.glebalekseevjk.auth.presentation.di.AuthDependencies
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.todo.presentation.di.TodoDependencies
import com.glebalekseevjk.todoapp.App
import com.glebalekseevjk.todoapp.MainActivity
import com.glebalekseevjk.todoapp.broadcastreceiver.BootCompleteReceiver
import com.glebalekseevjk.todoapp.broadcastreceiver.NotificationReceiver
import com.glebalekseevjk.todoapp.broadcastreceiver.SetAsideADayReceiver
import com.glebalekseevjk.todoapp.broadcastreceiver.TimeChangedReceiver
import com.glebalekseevjk.todoapp.di.module.AuthDependenciesModule
import com.glebalekseevjk.todoapp.di.module.BroadcastReceiverModule
import com.glebalekseevjk.todoapp.di.module.LocalDataSourceModule
import com.glebalekseevjk.todoapp.di.module.RemoteDataSourceModule
import com.glebalekseevjk.todoapp.di.module.RepositoryModule
import com.glebalekseevjk.todoapp.di.module.TodoDependenciesModule
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
import com.glebalekseevjk.todoapp.worker.SynchronizeWorker
import dagger.BindsInstance
import dagger.Component


@AppComponentScope
@Component(
    modules = [
        RepositoryModule::class,
        LocalDataSourceModule::class,
        RemoteDataSourceModule::class,
        AuthDependenciesModule::class,
        TodoDependenciesModule::class,
        BroadcastReceiverModule::class
    ]
)
interface AppComponent : AuthDependencies, TodoDependencies {
    fun inject(application: App)
    fun inject(bootCompleteReceiver: BootCompleteReceiver)
    fun inject(timeChangedReceiver: TimeChangedReceiver)
    fun inject(mainActivity: MainActivity)
    fun inject(synchronizeWorker: SynchronizeWorker)
    fun inject(notificationReceiver: NotificationReceiver)
    fun inject(setAsideADayReceiver: SetAsideADayReceiver)

    @Component.Factory
    interface Factory {
        fun create(
            @[BindsInstance ApplicationContext] context: Context
        ): AppComponent
    }
}