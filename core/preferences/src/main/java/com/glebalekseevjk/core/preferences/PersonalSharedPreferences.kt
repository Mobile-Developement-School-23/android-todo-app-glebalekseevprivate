package com.glebalekseevjk.core.preferences

import android.content.Context
import android.content.SharedPreferences
import com.glebalekseevjk.core.preferences.PersonalStorage.Companion.NightMode
import com.glebalekseevjk.core.preferences.exception.UnknownDeviceIdException
import com.glebalekseevjk.core.utils.di.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
Ответственность класса PersonalSharedPreferences:
Управление персональными настройками и данными пользователей.
Класс обеспечивает доступ к SharedPreferences и предоставляет методы
для чтения и записи значений, таких как редакция, токен,
дата последней синхронизации и идентификатор устройства.
Он также предоставляет методы для очистки всех значений.
Класс имеет единственную ответственность, связанную
с управлением персональными настройками и данными пользователей.
 */
class PersonalSharedPreferences @Inject constructor(@ApplicationContext private val context: Context) :
    PersonalStorage {
    private val personalPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)

    override var revision: String?
        get() = personalPreferences.getString(PREF_KEY_REVISION, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_REVISION, value).apply()
        }

    override var oauthToken: String?
        get() = personalPreferences.getString(PREF_KEY_OAUTH_TOKEN, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_OAUTH_TOKEN, value).apply()
        }

    override var bearerToken: String?
        get() = personalPreferences.getString(PREF_KEY_BEARER_TOKEN, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_BEARER_TOKEN, value).apply()
        }

    override var lastSynchronizationDate: Date
        get() = Date(
            personalPreferences.getLong(
                PREF_KEY_LAST_SYNCHRONIZATION_DATE,
                Calendar.getInstance().time.time
            )
        )
        set(value) {
            personalPreferences.edit().putLong(PREF_KEY_LAST_SYNCHRONIZATION_DATE, value.time)
                .apply()
        }

    override var deviceId: String
        get() = personalPreferences.getString(PREF_KEY_DEVICE_ID, null)
            ?: throw UnknownDeviceIdException()
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_DEVICE_ID, value).apply()
        }

    private val _isNightMode: MutableStateFlow<NightMode> = MutableStateFlow(
        personalPreferences.let {
            val nightMode = it.getString(PREF_KEY_NIGHT_MODE, null)
            nightMode ?:
                personalPreferences.edit().putString(PREF_KEY_NIGHT_MODE, NightMode.SYSTEM.name).apply()
            nightMode ?: return@let NightMode.SYSTEM
            NightMode.valueOf(nightMode)
        }
    )
    override val nightMode: Flow<NightMode> get() = _isNightMode
    override suspend fun setNightMode(isNightMode: NightMode) {
        personalPreferences.edit().putString(PREF_KEY_NIGHT_MODE, isNightMode.name).apply()
        _isNightMode.emit(isNightMode)
    }

    override fun clear() {
        personalPreferences.edit().remove(PREF_KEY_REVISION).apply()
        personalPreferences.edit().remove(PREF_KEY_OAUTH_TOKEN).apply()
        personalPreferences.edit().remove(PREF_KEY_BEARER_TOKEN).apply()
        personalPreferences.edit().remove(PREF_KEY_LAST_SYNCHRONIZATION_DATE).apply()
        personalPreferences.edit().remove(PREF_KEY_DEVICE_ID).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "com.glebalekseevjk.todoapp"
        private const val PREF_KEY_REVISION = "revision"
        private const val PREF_KEY_OAUTH_TOKEN = "oauth_token"
        private const val PREF_KEY_BEARER_TOKEN = "bearer_token"
        private const val PREF_KEY_LAST_SYNCHRONIZATION_DATE = "last_sync_date"
        private const val PREF_KEY_DEVICE_ID = "device_id"
        private const val PREF_KEY_NIGHT_MODE = "night_mode"
    }
}