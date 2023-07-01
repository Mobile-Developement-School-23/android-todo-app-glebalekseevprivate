package com.glebalekseevjk.todoapp.data.datasource.remote.interceptor

import com.glebalekseevjk.todoapp.data.datasource.preferences.PersonalSharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let {
            it.addTokenHeader() ?: it
        }
        .let { chain.proceed(it) }

    private fun Request.addTokenHeader(): Request? {
        val authHeaderName = "Authorization"
        val token = personalSharedPreferences.token ?: return null
        return newBuilder()
            .apply {
                header(authHeaderName, token.withOAuth())
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"
    private fun String.withOAuth() = "OAuth $this"
}