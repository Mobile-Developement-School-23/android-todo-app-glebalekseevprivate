package com.glebalekseevjk.domain.sync

interface SynchronizationRepository {
    val lastSyncDate: String
    suspend fun pull()
    suspend fun getSynchronizeState(): SynchronizeState

    interface SynchronizeState {
        val isSynchronized: Boolean
        suspend fun synchronize()
    }
}