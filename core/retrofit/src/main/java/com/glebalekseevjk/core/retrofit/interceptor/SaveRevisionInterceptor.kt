package com.glebalekseevjk.core.retrofit.interceptor

import com.glebalekseevjk.core.preferences.PersonalSharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.http.HTTP
import java.net.HttpURLConnection
import javax.inject.Inject

/**
Ответственность класса SaveRevisionInterceptor:
Класс SaveRevisionInterceptor отвечает за перехват и сохранение ревизии (revision) из HTTP-ответа.
Он выполняет проверку кода ответа, извлекает ревизию из тела ответа
и сохраняет ее в локальное хранилище PersonalSharedPreferences.
 */
class SaveRevisionInterceptor @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == HttpURLConnection.HTTP_OK) {
            val responseBody = response.peekBody(BYTE_COUNT).string()
            val revision: String? = responseBody.let {
                regex.find(it)?.groupValues?.get(1)
            }
            revision?.let { personalSharedPreferences.revision = it }
        }
        return response
    }

    companion object {
        private val regex = Regex("""\s*"revision"\s*:\s*(\d+).*""")
        private const val BYTE_COUNT = 2048L
    }
}