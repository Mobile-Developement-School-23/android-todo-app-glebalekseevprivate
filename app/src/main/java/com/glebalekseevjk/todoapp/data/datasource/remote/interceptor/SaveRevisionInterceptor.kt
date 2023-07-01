package com.glebalekseevjk.todoapp.data.datasource.remote.interceptor

import com.glebalekseevjk.todoapp.data.datasource.preferences.PersonalSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SaveRevisionInterceptor @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 200) {
            val responseBody = response.peekBody(2048).string()
            val revision: String? = responseBody.let {
                regex.find(it)?.groupValues?.get(1)
            }
            revision?.let { personalSharedPreferences.revision = it }
        }
        return response
    }

    companion object {
        private val regex = Regex("""\s*"revision"\s*:\s*(\d+).*""")
    }
}