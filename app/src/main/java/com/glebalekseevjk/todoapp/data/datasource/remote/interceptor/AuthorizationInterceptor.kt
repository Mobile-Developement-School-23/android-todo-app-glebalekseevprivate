package com.glebalekseevjk.todoapp.data.datasource.remote.interceptor

import com.glebalekseevjk.todoapp.data.datasource.preferences.PersonalSharedPreferences
import com.glebalekseevjk.todoapp.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences,
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let {
            it.addTokenHeader() ?: return Response.Builder()
                .code(401)
                .protocol(Protocol.HTTP_2)
                .request(it)
                .build()
                .also {
                    CoroutineScope(Dispatchers.IO).launch {
                        authRepository.unauthorize()
                    }
                }
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