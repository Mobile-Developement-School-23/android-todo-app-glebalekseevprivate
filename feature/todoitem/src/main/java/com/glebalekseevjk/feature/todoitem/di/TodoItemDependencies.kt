package com.glebalekseevjk.feature.todoitem.di

import android.content.Context
import android.content.Intent
import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.retrofit.model.TodoElement
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.domain.sync.SynchronizationRepository
import com.glebalekseevjk.domain.sync.SynchronizationSchedulerManager
import com.glebalekseevjk.core.utils.di.Dependencies
import com.glebalekseevjk.todoapp.domain.entity.entity.TodoItem
import javax.inject.Qualifier

interface TodoItemDependencies: Dependencies {
    val authRepository: AuthRepository
    val schedulerManager: SynchronizationSchedulerManager
    val synchronizationRepository: SynchronizationRepository
    val context: Context
    val todoItemDao: TodoItemDao
    val todRemoveTodoItemDao: ToRemoveTodoItemDao
    val mapper: Mapper<TodoItem, TodoItemDbModel>
}
