package com.glebalekseevjk.auth.domain.repository

import com.glebalekseevjk.auth.domain.entity.NightMode
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PersonalRepository {
    var revision: String?
    var oauthToken: String?
    var bearerToken: String?
    var lastSynchronizationDate: Date
    var deviceId: String?
    val nightModeAsFlow: Flow<NightMode>
    val nightMode: NightMode
    suspend fun setNightMode(nightMode: NightMode)
    fun clear()
}