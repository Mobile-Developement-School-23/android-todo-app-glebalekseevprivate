package com.glebalekseevjk.todoapp.data.datasource.preferences

import android.content.Context
import android.content.SharedPreferences
import java.util.Date
import javax.inject.Inject

class PersonalSharedPreferences @Inject constructor(context: Context) {
    private val personalPreferences: SharedPreferences

    init {
        personalPreferences = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    var revision: String?
        get() = personalPreferences.getString(PREF_KEY_REVISION, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_REVISION, value).apply()
        }

    var token: String?
        get() = personalPreferences.getString(PREF_KEY_TOKEN, null)
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_TOKEN, value).apply()
        }

    var lastSynchronizationDate: Date
        get() = Date(personalPreferences.getLong(PREF_KEY_LAST_SYNCHRONIZATION_DATE, 0L))
        set(value) {
            personalPreferences.edit().putLong(PREF_KEY_LAST_SYNCHRONIZATION_DATE, value.time)
                .apply()
        }

    var deviceId: String
        get() = personalPreferences.getString(PREF_KEY_DEVICE_ID, null)
            ?: throw RuntimeException("Device id is not set")
        set(value) {
            personalPreferences.edit().putString(PREF_KEY_DEVICE_ID, value).apply()
        }

    fun clear() {
        personalPreferences.edit().remove(PREF_KEY_REVISION).apply()
        personalPreferences.edit().remove(PREF_KEY_TOKEN).apply()
        personalPreferences.edit().remove(PREF_KEY_LAST_SYNCHRONIZATION_DATE).apply()
        personalPreferences.edit().remove(PREF_KEY_DEVICE_ID).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "com.glebalekseevjk.todoapp"
        private const val PREF_KEY_REVISION = "revision"
        private const val PREF_KEY_TOKEN = "token"
        private const val PREF_KEY_LAST_SYNCHRONIZATION_DATE = "last_sync_date"
        private const val PREF_KEY_DEVICE_ID = "device_id"
    }
}