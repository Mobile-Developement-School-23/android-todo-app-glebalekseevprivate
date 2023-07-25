package com.glebalekseevjk.todo.data.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.glebalekseevjk.core.utils.exception.AuthorizationException
import com.glebalekseevjk.core.utils.exception.ClientException
import com.glebalekseevjk.core.utils.exception.ConnectionException
import com.glebalekseevjk.core.utils.exception.ServerException
import com.glebalekseevjk.core.utils.exception.UnknownException
import retrofit2.Response
import java.net.HttpURLConnection

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
    isInternetAvailable: () -> Boolean = { true },
) {
    try {
        val response = onRequest.invoke()
        when (response.code()) {
            HttpURLConnection.HTTP_OK -> {
                onSuccessful.invoke(response)
            }

            HttpURLConnection.HTTP_BAD_REQUEST -> {
                onBadRequest.invoke()
            }

            HttpURLConnection.HTTP_UNAUTHORIZED -> throw AuthorizationException()
            HttpURLConnection.HTTP_NOT_FOUND -> {
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