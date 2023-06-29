package com.glebalekseevjk.todoapp.data.repository

import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.preferences.PersonalSharedPreferences
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import javax.inject.Inject

@AppComponentScope
class SynchronizationRepository @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences,
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao
){

}