package com.glebalekseevjk.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isAuthAsFlow: Flow<Boolean>
    suspend fun init(value: Boolean)
    suspend fun authorize()
    suspend fun quit()
}