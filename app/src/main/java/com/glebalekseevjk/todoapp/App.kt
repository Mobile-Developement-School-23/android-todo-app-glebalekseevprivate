package com.glebalekseevjk.todoapp

import android.app.Application
import android.content.Context
import com.glebalekseevjk.todoapp.ioc.AppComponent
import com.glebalekseevjk.todoapp.ioc.DaggerAppComponent

class App: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.injectApp(this)
    }

    companion object {
        fun get(context: Context): App = context.applicationContext as App
    }
}