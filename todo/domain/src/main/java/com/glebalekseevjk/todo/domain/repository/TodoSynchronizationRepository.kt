package com.glebalekseevjk.todo.domain.repository

import java.util.Date

interface TodoSynchronizationRepository {
    val lastSyncDate: Date
    suspend fun pull()
    suspend fun getSynchronizeState(): SynchronizeState

    interface SynchronizeState {
        val isSynchronized: Boolean
        suspend fun synchronize()
    }
}