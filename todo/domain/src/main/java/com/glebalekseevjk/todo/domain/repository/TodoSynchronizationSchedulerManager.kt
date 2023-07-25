package com.glebalekseevjk.todo.domain.repository

interface TodoSynchronizationSchedulerManager {
    fun setupPeriodicSynchronize()
    fun setupOneTimeSynchronize()
}