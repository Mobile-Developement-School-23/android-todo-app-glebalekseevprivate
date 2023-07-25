package com.glebalekseevjk.auth.domain.usecase

import com.glebalekseevjk.auth.domain.repository.AuthRepository
import com.glebalekseevjk.auth.domain.repository.PersonalRepository
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class AuthUseCase @Inject constructor (
    private val authRepository: AuthRepository,
    private val personalRepository: PersonalRepository,
    private val todoItemRepository: TodoItemRepository,
) {
    init {
        CoroutineScope(Dispatchers.Default).launch {
            authRepository.init(isAuth)
        }
    }

    val isAuthAsFlow: Flow<Boolean> get() = authRepository.isAuthAsFlow

    suspend fun oauthAuthorization(oauthToken: String) {
        withContext(Dispatchers.Default){
            personalRepository.oauthToken = oauthToken
            authRepository.authorize()
        }
    }

    suspend fun bearerAuthorization(bearerToken: String) {
        withContext(Dispatchers.Default){
            personalRepository.bearerToken = bearerToken
            authRepository.authorize()
        }
    }

    suspend fun quit() {
        withContext(Dispatchers.Default){
            authRepository.quit()
            delay(200)
            personalRepository.clear()
            todoItemRepository.clear()
        }
    }

    private val isAuth get() = !(personalRepository.bearerToken != null && personalRepository.oauthToken != null ||
            personalRepository.bearerToken == null && personalRepository.oauthToken == null)
}