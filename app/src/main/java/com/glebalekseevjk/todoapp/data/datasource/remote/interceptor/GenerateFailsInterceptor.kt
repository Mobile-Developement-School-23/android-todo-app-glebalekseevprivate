package com.glebalekseevjk.todoapp.data.datasource.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class GenerateFailsInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .addRevisionHeader()
        .let { chain.proceed(it) }

    private fun Request.addRevisionHeader(): Request {
        val headerName = "X-Generate-Fails"
        return newBuilder()
            .apply {
                header(headerName, "20")
            }
            .build()
    }
}