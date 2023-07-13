package com.glebalekseevjk.core.retrofit.interceptor

import com.glebalekseevjk.core.preferences.PersonalStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
Ответственность класса AuthorizationInterceptor:
Класс AuthorizationInterceptor отвечает за перехват и обработку
авторизационных заголовков в запросах. Он предоставляет функциональность добавления
токена авторизации к заголовкам запроса, используя SharedPreferences для получения токена.
 */
class AuthorizationInterceptor @Inject constructor(
    private val personalStorage: PersonalStorage,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let {
            it.addTokenHeader() ?: it
        }
        .let { chain.proceed(it) }

    private fun Request.addTokenHeader(): Request? {
        val authHeaderName = "Authorization"
        val token = personalStorage.token ?: return null
        return newBuilder()
            .apply {
                header(authHeaderName, token.withOAuth())
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"
    private fun String.withOAuth() = "OAuth $this"
}