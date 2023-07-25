package com.glebalekseevjk.todo.data.retrofit.interceptor

import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

/**
Этот класс сохраняет ревизию (revision) в локальном хранилище.
 */
class SaveRevisionInterceptor @Inject constructor(
    private val personalUseCase: PersonalUseCase
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == HttpURLConnection.HTTP_OK) {
            val responseBody = response.peekBody(BYTE_COUNT).string()
            val revision = Gson().fromJson(responseBody, RevisionCatcher::class.java).revision
            revision?.let { personalUseCase.revision = revision }
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