package com.glebalekseevjk.todo.data.retrofit.interceptor

import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
Этот класс добавляет в HTTP запрос ревизию (revision).
 */
class RevisionInterceptor @Inject constructor(
    private val personalUseCase: PersonalUseCase
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let {
            if (it.method != "GET") it.addRevisionHeader() ?: it
            else it
        }
        .let { chain.proceed(it) }

    private fun Request.addRevisionHeader(): Request? {
        val revisionHeaderName = "X-Last-Known-Revision"
        val revision = personalUseCase.revision ?: return null
        return newBuilder()
            .apply {
                header(revisionHeaderName, revision)
            }
            .build()
    }
}