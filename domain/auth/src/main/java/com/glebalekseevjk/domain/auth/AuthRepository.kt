package com.glebalekseevjk.domain.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isAuth: Flow<Boolean>
    suspend fun authorize(token: String)
    suspend fun quit()
}