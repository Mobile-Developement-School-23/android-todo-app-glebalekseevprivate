package com.glebalekseevjk.auth.data.repository

import com.glebalekseevjk.auth.data.storage.PersonalStorage
import com.glebalekseevjk.auth.domain.entity.NightMode
import com.glebalekseevjk.auth.domain.repository.PersonalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
Этот репозиторий необходим для управления хранилищем конфигурации пользователя.
Хранит поток состояния ночного режима.
 */

class PersonalRepositoryImpl @Inject constructor(
    private val personalStorage: PersonalStorage
) : PersonalRepository {
    override var revision: String?
        get() = personalStorage.revision
        set(value) {
            personalStorage.revision = value
        }
    override var oauthToken: String?
        get() = personalStorage.oauthToken
        set(value) {
            personalStorage.oauthToken = value
        }
    override var bearerToken: String?
        get() = personalStorage.bearerToken
        set(value) {
            personalStorage.bearerToken = value
        }
    override var lastSynchronizationDate: Date
        get() = Date(personalStorage.lastSynchronization ?: Calendar.getInstance().time.time)
        set(value) {
            personalStorage.lastSynchronization = value.time
        }
    override var deviceId: String?
        get() = personalStorage.deviceId
        set(value) {
            personalStorage.deviceId = value
        }
    override val nightModeAsFlow: Flow<NightMode>
        get() = _isNightMode
    private val _isNightMode: MutableStateFlow<NightMode> = MutableStateFlow(
        personalStorage.let {
            val nightMode = it.nightMode
            nightMode ?: return@let NightMode.SYSTEM.also { personalStorage.nightMode = it.name }
            NightMode.valueOf(nightMode)
        }
    )
    override val nightMode: NightMode
        get() = _isNightMode.value

    override suspend fun setNightMode(nightMode: NightMode) {
        personalStorage.nightMode = nightMode.name
        _isNightMode.emit(nightMode)
    }

    override fun clear() = personalStorage.clear()
}