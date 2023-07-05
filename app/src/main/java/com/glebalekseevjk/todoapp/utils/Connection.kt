package com.glebalekseevjk.todoapp.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.glebalekseevjk.todoapp.domain.entity.exception.AuthorizationException
import com.glebalekseevjk.todoapp.domain.entity.exception.ClientException
import com.glebalekseevjk.todoapp.domain.entity.exception.ConnectionException
import com.glebalekseevjk.todoapp.domain.entity.exception.ServerException
import com.glebalekseevjk.todoapp.domain.entity.exception.UnknownException
import retrofit2.Response

fun ConnectivityManager.isInternetAvailable(): Boolean {
    val network = this.activeNetwork
    val networkCapabilities = this.getNetworkCapabilities(network)
    return networkCapabilities != null &&
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}

suspend fun <R> handleResponse(
    onRequest: suspend () -> Response<R>,
    onSuccessful: (response: Response<R>) -> Unit = {},
    onNotFound: () -> Unit = { throw ClientException() },
    onBadRequest: suspend () -> Unit = { throw ClientException() },
    isInternetAvailable: ()->Boolean = { true },
) {
    try {
        val response = onRequest.invoke()
        when (response.code()) {
            200 -> {
                onSuccessful.invoke(response)
            }

            400 -> {
                onBadRequest.invoke()
            }

            401 -> throw AuthorizationException()
            404 -> {
                onNotFound.invoke()
            }

            in 400..499 -> throw ClientException()
            in 500..599 -> throw ServerException()
            else -> throw UnknownException()
        }
    } catch (e: Exception) {
        println(e)
        if (!isInternetAvailable.invoke()) throw ConnectionException()
        else throw e
    }
}