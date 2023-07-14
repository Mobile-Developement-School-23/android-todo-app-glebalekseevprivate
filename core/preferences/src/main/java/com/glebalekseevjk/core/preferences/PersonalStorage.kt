package com.glebalekseevjk.core.preferences

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PersonalStorage {
    var revision: String?
    var token: String?
    var lastSynchronizationDate: Date
    var deviceId: String
    val nightMode: Flow<NightMode>
    suspend fun setNightMode(isNightMode: NightMode)
    fun clear()
    companion object {
        enum class NightMode {
            NIGHT,
            DAY,
            SYSTEM
        }
    }
}