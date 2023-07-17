package com.glebalekseevjk.core.utils

import okhttp3.OkHttpClient
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Отключение проверки сертификатов для возможности отслеживать трафик приложения.
 */
fun OkHttpClient.Builder.withDisabledCertificates(): OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(
            chain: Array<out java.security.cert.X509Certificate>?,
            authType: String?
        ) {}

        override fun checkServerTrusted(
            chain: Array<out java.security.cert.X509Certificate>?,
            authType: String?
        ) {}

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }
    })
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())
    return this
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
}