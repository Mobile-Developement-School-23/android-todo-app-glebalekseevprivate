package com.glebalekseevjk.todoapp.utils

import android.content.Context
import android.widget.Toast

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Failure<T>(val throwable: Throwable) : Resource<T>()
}

fun <T> Resource<T>.checkFailure(): T {
    when (this) {
        is Resource.Failure -> throw this.throwable
        is Resource.Success -> return this.data
    }
}

// only debug
fun <T> Resource<T>.printFailure(context: Context): T? =
    when (this) {
        is Resource.Failure -> {
            Toast.makeText(
                context,
                "Error: ${this.throwable.cause} message: ${this.throwable.message}",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

        is Resource.Success -> this.data
    }

