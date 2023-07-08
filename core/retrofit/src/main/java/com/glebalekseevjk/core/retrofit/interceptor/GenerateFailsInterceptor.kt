package com.glebalekseevjk.core.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
/**
Ответственность класса GenerateFailsInterceptor:
Данный класс является интерцептором для выполнения определенных
операций перед отправкой запроса и после получения ответа.
В данном случае, класс добавляет заголовок "X-Generate-Fails" со значением "20"
к исходному запросу перед его отправкой. Класс отвечает за генерацию заголовка "X-Generate-Fails"
и несет ответственность за его добавление к запросу.
 */
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