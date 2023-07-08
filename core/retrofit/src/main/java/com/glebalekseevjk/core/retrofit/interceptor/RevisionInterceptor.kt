package com.glebalekseevjk.core.retrofit.interceptor

import com.glebalekseevjk.core.preferences.PersonalSharedPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.HttpMethod
import java.net.HttpURLConnection
import javax.inject.Inject

class RevisionInterceptor @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let {
            if (it.method != "GET") it.addRevisionHeader() ?: it
            else it
        }
        .let { chain.proceed(it) }

    private fun Request.addRevisionHeader(): Request? {
        val revisionHeaderName = "X-Last-Known-Revision"
        val revision = personalSharedPreferences.revision ?: return null
        return newBuilder()
            .apply {
                header(revisionHeaderName, revision)
            }
            .build()
    }
}