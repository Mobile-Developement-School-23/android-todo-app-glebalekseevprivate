package com.glebalekseevjk.todoapp.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.glebalekseevjk.core.utils.Constants.WORKER_SYNCHRONIZATION_TIMEOUT
import com.glebalekseevjk.domain.sync.SynchronizationSchedulerManager
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AppComponentScope
class SynchronizationSchedulerManagerImpl @Inject constructor(context: Context) :
    SynchronizationSchedulerManager {
    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    override fun setupPeriodicSynchronize() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<SynchronizeWorker>(
            WORKER_SYNCHRONIZATION_TIMEOUT,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            SynchronizeWorker.SYNCHRONIZE_PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    override fun setupOneTimeSynchronize() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val oneTimeRequest = OneTimeWorkRequestBuilder<SynchronizeWorker>()
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniqueWork(
            SynchronizeWorker.SYNCHRONIZE_ONE_TIME_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            oneTimeRequest
        )
    }
}