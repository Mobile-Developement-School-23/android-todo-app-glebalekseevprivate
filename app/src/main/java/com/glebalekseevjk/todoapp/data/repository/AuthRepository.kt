package com.glebalekseevjk.todoapp.data.repository

import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.preferences.PersonalSharedPreferences
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@AppComponentScope
class AuthRepository @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences,
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
) {
    private val _isAuth = MutableStateFlow(!personalSharedPreferences.token.isNullOrEmpty())
    val isAuth: Flow<Boolean> get() = _isAuth
    suspend fun authorize(token: String) {
        withContext(Dispatchers.Default) {
            personalSharedPreferences.token = token
            personalSharedPreferences.deviceId = UUID.randomUUID().toString()
            _isAuth.emit(true)
        }
    }

    suspend fun quit() {
        withContext(Dispatchers.Default) {
            personalSharedPreferences.clear()
            todoItemDao.deleteAll()
            toRemoveTodoItemDao.deleteAll()
            _isAuth.emit(false)
        }
    }
}