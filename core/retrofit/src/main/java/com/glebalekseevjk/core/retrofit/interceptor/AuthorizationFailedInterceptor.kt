package com.glebalekseevjk.core.retrofit.interceptor

import com.glebalekseevjk.domain.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject
/**
Ответственность класса AuthorizationFailedInterceptor: Обработка неудачной авторизации в сетевых запросах.
Класс перехватывает ответ от сервера, проверяет код ответа, и если он соответствует HTTP_UNAUTHORIZED,
выполняет выход пользователя из приложения, используя AuthRepository.
 */
class AuthorizationFailedInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            CoroutineScope(Dispatchers.IO).launch {
                authRepository.quit()
            }
        }
        return response
    }
}