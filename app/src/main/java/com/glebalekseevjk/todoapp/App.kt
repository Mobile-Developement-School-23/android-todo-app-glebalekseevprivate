package com.glebalekseevjk.todoapp

import android.app.Application
import android.content.Context
import com.glebalekseevjk.todoapp.ioc.AppComponent
import com.glebalekseevjk.todoapp.ioc.DaggerAppComponent
import com.glebalekseevjk.todoapp.worker.SchedulerManager
import javax.inject.Inject

class App: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.injectApp(this)
    }

    @Inject
    fun setupWorkers(schedulerManager: SchedulerManager){
        schedulerManager.setupPeriodicSynchronize()
    }

    companion object {
        fun get(context: Context): App = context.applicationContext as App
    }
}