package com.glebalekseevjk.todoapp.data.repository

import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.remote.TodoItemService
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope

@AppComponentScope
class TodoItemRepository(
    private val todoItemService: TodoItemService,
    private val todoItemDao: TodoItemDao,
){

}