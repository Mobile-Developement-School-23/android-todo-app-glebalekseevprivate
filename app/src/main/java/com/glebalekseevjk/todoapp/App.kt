package com.glebalekseevjk.todoapp

import android.app.Application
import com.glebalekseevjk.core.utils.di.DepsMap
import com.glebalekseevjk.core.utils.di.HasDependencies
import com.glebalekseevjk.domain.sync.SynchronizationSchedulerManager
import com.glebalekseevjk.todoapp.di.AppComponent
import com.glebalekseevjk.todoapp.di.DaggerAppComponent
import javax.inject.Inject


/**
 * Ответственность класса App: инициализации и настройке графа зависимостей приложения,
 * а также настройка периодической синхронизации через внедрение зависимостей
 */
class App : Application(), HasDependencies {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(this)
    }

    @Inject
    override lateinit var depsMap: DepsMap

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    @Inject
    fun setupWorkers(synchronizationSchedulerManager: SynchronizationSchedulerManager) {
        synchronizationSchedulerManager.setupPeriodicSynchronize()
    }
}