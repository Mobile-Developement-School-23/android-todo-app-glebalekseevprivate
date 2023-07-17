package com.glebalekseevjk.core.retrofit.interceptor

import com.glebalekseevjk.core.preferences.PersonalStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
Ответственность класса RevisionInterceptor:
Интерцептор ревизий, который добавляет заголовок с последней известной ревизией к запросам,
кроме GET-запросов. Если ревизия доступна в PersonalSharedPreferences,
то заголовок "X-Last-Known-Revision" будет добавлен к запросу.
 */
class RevisionInterceptor @Inject constructor(
    private val personalStorage: PersonalStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let {
            if (it.method != "GET") it.addRevisionHeader() ?: it
            else it
        }
        .let { chain.proceed(it) }

    private fun Request.addRevisionHeader(): Request? {
        val revisionHeaderName = "X-Last-Known-Revision"
        val revision = personalStorage.revision ?: return null
        return newBuilder()
            .apply {
                header(revisionHeaderName, revision)
            }
            .build()
    }
}