package com.glebalekseevjk.todo.data.retrofit.interceptor

import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
Этот класс добавляет в HTTP запрос токен авторизации.
 */
class AuthorizationInterceptor @Inject constructor(
    private val personalUseCase: PersonalUseCase,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let {
            it.addTokenHeader() ?: it
        }
        .let { chain.proceed(it) }

    private fun Request.addTokenHeader(): Request? {
        val authHeaderName = "Authorization"
        val bearerToken = personalUseCase.bearerToken
        val oauthToken = personalUseCase.oauthToken

        val bothTokensSet = bearerToken != null && oauthToken != null
        val bothTokensNotSet = bearerToken == null && oauthToken == null
        if (bothTokensSet || bothTokensNotSet) return null

        return newBuilder()
            .apply {
                bearerToken?.let { header(authHeaderName, bearerToken.withBearer()) }
                oauthToken?.let { header(authHeaderName, oauthToken.withOAuth()) }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"
    private fun String.withOAuth() = "OAuth $this"
}