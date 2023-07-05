package com.glebalekseevjk.todoapp.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AppComponentScope
class SchedulerManager @Inject constructor(context: Context) {
    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    fun setupPeriodicSynchronize() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val repeatingRequest = PeriodicWorkRequestBuilder<SynchronizeWorker>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            SynchronizeWorker.SYNCHRONIZE_PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    fun setupOneTimeSynchronize() {
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