package com.glebalekseevjk.core.retrofit.interceptor

import com.glebalekseevjk.core.preferences.PersonalStorage
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

/**
Ответственность класса SaveRevisionInterceptor:
Класс SaveRevisionInterceptor отвечает за перехват и сохранение ревизии (revision) из HTTP-ответа.
Он выполняет проверку кода ответа, извлекает ревизию из тела ответа
и сохраняет ее в локальное хранилище PersonalSharedPreferences.
 */
class SaveRevisionInterceptor @Inject constructor(
    private val personalStorage: PersonalStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == HttpURLConnection.HTTP_OK) {
            val responseBody = response.peekBody(BYTE_COUNT).string()
            val revision = Gson().fromJson(responseBody, RevisionCatcher::class.java).revision
            revision?.let { personalStorage.revision = revision }
        }
        return response
    }

    companion object {
        private const val BYTE_COUNT = 1048576L // 1 MiB
    }
}

private data class RevisionCatcher(
    val revision: String?
)