package com.glebalekseevjk.todoapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationRepository
import com.glebalekseevjk.todoapp.utils.appComponent
import javax.inject.Inject


/**
 * Этот класс представляет [CoroutineWorker] для синхронизации данных.
 */
class SynchronizeWorker(
    @ApplicationContext
    private val appContext: Context,
    params: WorkerParameters
) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var synchronizationRepository: TodoSynchronizationRepository

    override suspend fun doWork(): Result {
        return try {
            appContext.appComponent.inject(this)
            val synchronizeState = synchronizationRepository.getSynchronizeState()
            if (synchronizeState.isSynchronized) {
                synchronizationRepository.pull()
            } else {
                synchronizeState.synchronize()
            }
            Result.success()
        } catch (e: Exception) {
            println(e)
            Result.failure()
        }
    }

    companion object {
        const val SYNCHRONIZE_ONE_TIME_WORK_NAME = "synchronize_one_time_worker"
        const val SYNCHRONIZE_PERIODIC_WORK_NAME = "synchronize_periodic_worker"
    }
}
