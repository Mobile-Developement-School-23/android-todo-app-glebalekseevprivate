package com.glebalekseevjk.todoapp.data.repository

import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.preferences.PersonalSharedPreferences
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.Date
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
        personalSharedPreferences.token = token
        _isAuth.emit(true)
    }

    suspend fun unauthorize() {
        personalSharedPreferences.clear()
        todoItemDao.deleteAll()
        toRemoveTodoItemDao.deleteAll()
        _isAuth.emit(false)
    }
}