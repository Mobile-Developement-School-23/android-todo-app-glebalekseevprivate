package com.glebalekseevjk.todo.data.retrofit.interceptor

import com.glebalekseevjk.auth.domain.usecase.AuthUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

/**
Этот класс проверяет ответ сервера на наличие кода 401 (Unauthorized).
При необходимости разлогинивает пользователя.
 */
class AuthorizationFailedInterceptor @Inject constructor(
    private val authUseCase: AuthUseCase
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            CoroutineScope(Dispatchers.IO).launch {
                authUseCase.quit()
            }
        }
        return response
    }
}