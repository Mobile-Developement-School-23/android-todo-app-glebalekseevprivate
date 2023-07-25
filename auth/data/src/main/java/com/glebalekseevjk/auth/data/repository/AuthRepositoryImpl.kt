package com.glebalekseevjk.auth.data.repository

import com.glebalekseevjk.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
Этот репозиторий представляет поток состояния авторизованности
 */
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val _isAuth = MutableStateFlow(false)
    override val isAuthAsFlow: Flow<Boolean> get() = _isAuth

    override suspend fun init(value: Boolean) {
        _isAuth.emit(value)
    }

    override suspend fun authorize() {
        _isAuth.emit(true)
    }

    override suspend fun quit() {
        _isAuth.emit(false)
    }
}