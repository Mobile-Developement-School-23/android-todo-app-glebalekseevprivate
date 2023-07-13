package com.glebalekseevjk.data.auth

import com.glebalekseevjk.core.preferences.PersonalSharedPreferences
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.domain.auth.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

/**
Ответственность класса AuthRepositoryImpl:
Класс AuthRepositoryImpl отвечает за реализацию интерфейса AuthRepository
и предоставляет функциональность для авторизации и выхода пользователя.
Он обрабатывает операции с персональными настройками, данными задач
и управлением состоянием авторизации. Его основная ответственность заключается в сохранении
и удалении данных пользователя,
а также в обновлении состояния авторизации в соответствии с выполненными операциями.
 */
class AuthRepositoryImpl @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences,
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
) : AuthRepository {
    private val _isAuth = MutableStateFlow(!personalSharedPreferences.token.isNullOrEmpty())
    override val isAuth: Flow<Boolean> get() = _isAuth
    override suspend fun authorize(token: String) {
        withContext(Dispatchers.Default) {
            personalSharedPreferences.token = token
            personalSharedPreferences.deviceId = UUID.randomUUID().toString()
            _isAuth.emit(true)
        }
    }

    override suspend fun quit() {
        withContext(Dispatchers.Default) {
            _isAuth.emit(false)
            delay(200)
            personalSharedPreferences.clear()
            todoItemDao.deleteAll()
            toRemoveTodoItemDao.deleteAll()
        }
    }
}