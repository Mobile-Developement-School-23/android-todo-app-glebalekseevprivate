package com.glebalekseevjk.auth.domain.usecase

import com.glebalekseevjk.auth.domain.entity.NightMode
import com.glebalekseevjk.auth.domain.repository.PersonalRepository
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class PersonalUseCase @Inject constructor(
    private val personalRepository: PersonalRepository,
) {
    val nightMode get() = personalRepository.nightMode
    val nightModeAsFlow get() = personalRepository.nightModeAsFlow

    val bearerToken: String?
        get() = personalRepository.bearerToken

    val oauthToken: String?
        get() = personalRepository.oauthToken

    var revision: String?
        get() = personalRepository.revision
        set(value) = personalRepository.run { revision = value }

    val deviceId: String
        get() = personalRepository.deviceId ?: let {
            val uuid = UUID.randomUUID().toString()
            personalRepository.deviceId = uuid
            uuid
        }

    var lastSynchronizationDate: Date
        get() = personalRepository.lastSynchronizationDate
        set(value) = personalRepository.run { lastSynchronizationDate = value }

    suspend fun setNightMode(nightMode: NightMode) = personalRepository.setNightMode(nightMode)
}