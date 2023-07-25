package com.glebalekseevjk.auth.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.glebalekseevjk.core.utils.di.ApplicationContext
import javax.inject.Inject

/**
Это хранилище предназначено для конфигурации пользователя
 */
class PersonalStorage @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val personalPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)

    var revision: String?
        get() = personalPreferences.getString(PREF_KEY_REVISION, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_REVISION, value).apply()
        }

    var oauthToken: String?
        get() = personalPreferences.getString(PREF_KEY_OAUTH_TOKEN, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_OAUTH_TOKEN, value).apply()
        }

    var bearerToken: String?
        get() = personalPreferences.getString(PREF_KEY_BEARER_TOKEN, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_BEARER_TOKEN, value).apply()
        }

    var lastSynchronization: Long?
        get() =
            personalPreferences.getString(
                PREF_KEY_LAST_SYNCHRONIZATION_DATE,
                null
            )?.toLongOrNull()
        set(value) {
            personalPreferences.edit()
                .putString(PREF_KEY_LAST_SYNCHRONIZATION_DATE, value.toString())
                .apply()
        }

    var deviceId: String?
        get() = personalPreferences.getString(PREF_KEY_DEVICE_ID, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_DEVICE_ID, value).apply()
        }

    var nightMode: String?
        get() = personalPreferences.getString(PREF_KEY_NIGHT_MODE, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_NIGHT_MODE, value).apply()
        }

    fun clear() {
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