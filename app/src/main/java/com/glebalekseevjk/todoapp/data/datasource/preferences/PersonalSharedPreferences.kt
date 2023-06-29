package com.glebalekseevjk.todoapp.data.datasource.preferences

import android.content.Context
import android.content.SharedPreferences
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

    fun clear(){
        personalPreferences.edit().remove(PREF_KEY_REVISION).apply()
        personalPreferences.edit().remove(PREF_KEY_TOKEN).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "com.glebalekseevjk.yasmrhomework"
        private const val PREF_KEY_REVISION = "revision"
        private const val PREF_KEY_TOKEN = "token"
    }
}