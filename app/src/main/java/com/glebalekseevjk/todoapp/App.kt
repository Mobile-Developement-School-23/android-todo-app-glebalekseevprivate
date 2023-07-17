package com.glebalekseevjk.todoapp

import android.app.Application
import com.glebalekseevjk.core.utils.di.DepsMap
import com.glebalekseevjk.core.utils.di.HasDependencies
import com.glebalekseevjk.domain.sync.SynchronizationSchedulerManager
import com.glebalekseevjk.todoapp.di.AppComponent
import com.glebalekseevjk.todoapp.di.DaggerAppComponent
import javax.inject.Inject


/**
 * Этот класс символизирует период жизни приложения.
 *
 * Устанавливает периодическую синхронизацию с помощью [SynchronizationSchedulerManager]
 *
 * @property appComponent ссылка на компонент Dagger 2 [AppComponent].
 * @property depsMap словарь зависимостей предоставляемых текущим классом.
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