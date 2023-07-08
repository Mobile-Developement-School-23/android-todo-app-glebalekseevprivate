package com.glebalekseevjk.domain.sync

interface SynchronizationSchedulerManager {
    fun setupPeriodicSynchronize()
    fun setupOneTimeSynchronize()
}