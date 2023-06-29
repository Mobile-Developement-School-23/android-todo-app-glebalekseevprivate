package com.glebalekseevjk.todoapp.ioc

import android.content.Context
import com.glebalekseevjk.todoapp.App
import com.glebalekseevjk.todoapp.ioc.module.LocalDataSourceModule
import com.glebalekseevjk.todoapp.ioc.module.RemoteDataSourceModule
import com.glebalekseevjk.todoapp.ioc.module.ViewModelModule
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import com.glebalekseevjk.todoapp.presentation.activity.AuthActivity
import com.glebalekseevjk.todoapp.presentation.activity.MainActivity
import dagger.BindsInstance
import dagger.Component

@AppComponentScope
@Component(modules = [ViewModelModule::class, LocalDataSourceModule::class, RemoteDataSourceModule::class])
interface AppComponent {
    fun createAuthActivitySubcomponent(): AuthActivitySubcomponent
    fun injectApp(application: App)
    fun injectMainActivity(mainActivity: MainActivity)

    @Component.Factory
    interface Builder {
        fun create(@BindsInstance context: Context): AppComponent
    }
}