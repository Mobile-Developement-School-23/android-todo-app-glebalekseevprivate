package com.glebalekseevjk.todoapp.data.datasource.remote.interceptor

import com.glebalekseevjk.todoapp.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationFailedInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            CoroutineScope(Dispatchers.IO).launch {
                authRepository.quit()
            }
        }
        return response
    }
}