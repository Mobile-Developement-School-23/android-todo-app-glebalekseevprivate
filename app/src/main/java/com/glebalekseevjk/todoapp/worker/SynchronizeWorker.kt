package com.glebalekseevjk.todoapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.glebalekseevjk.domain.sync.SynchronizationRepository
import com.glebalekseevjk.todoapp.utils.appComponent
import javax.inject.Inject

/**
Ответственность класса SynchronizeWorker:
Класс SynchronizeWorker отвечает за выполнение синхронизации данных.
Он получает контекст приложения и параметры рабочего процесса,
используя библиотеку CoroutineWorker. Класс инъектирует репозиторий синхронизации
и выполняет синхронизацию данных в зависимости от состояния синхронизации.
Если данные уже синхронизированы, выполняется операция извлечения данных.
В противном случае, выполняется операция синхронизации данных.
 */
class SynchronizeWorker(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var synchronizationRepository: SynchronizationRepository

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
