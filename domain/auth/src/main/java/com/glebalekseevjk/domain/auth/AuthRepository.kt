package com.glebalekseevjk.domain.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isAuth: Flow<Boolean>
    suspend fun bearerAuthorization(bearerToken: String)
    suspend fun oauthAuthorization(oauthToken: String)
    suspend fun quit()
}