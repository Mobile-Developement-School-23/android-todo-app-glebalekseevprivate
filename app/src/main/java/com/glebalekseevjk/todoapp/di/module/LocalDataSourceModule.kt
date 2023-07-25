package com.glebalekseevjk.todoapp.di.module

import android.content.Context
import androidx.room.Room
import com.glebalekseevjk.auth.data.repository.PersonalRepositoryImpl
import com.glebalekseevjk.auth.domain.repository.PersonalRepository
import com.glebalekseevjk.core.utils.Mapper
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.todo.data.AppDatabase
import com.glebalekseevjk.todo.data.retrofit.mapper.TodoElementMapperImpl
import com.glebalekseevjk.todo.data.retrofit.model.TodoElement
import com.glebalekseevjk.todo.data.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todo.data.room.dao.TodoEventNotificationDao
import com.glebalekseevjk.todo.data.room.dao.TodoItemDao
import com.glebalekseevjk.todo.data.room.mapper.TodoEventNotificationMapperImpl
import com.glebalekseevjk.todo.data.room.mapper.TodoItemDbModelMapperImpl
import com.glebalekseevjk.todo.data.room.model.TodoEventNotificationDbModel
import com.glebalekseevjk.todo.data.room.model.TodoItemDbModel
import com.glebalekseevjk.todo.domain.entity.TodoEventNotification
import com.glebalekseevjk.todo.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface LocalDataSourceModule {
    @AppComponentScope
    @Binds
    fun bindPersonalStorage(
        personalSharedPreferences: PersonalRepositoryImpl
    ): PersonalRepository

    @Binds
    fun bindTodoElementMapperImpl(
        todoElementMapperImpl: TodoElementMapperImpl
    ): Mapper<TodoItem, TodoElement>

    @Binds
    fun bindEventNotificationMapperImpl(
        eventNotificationMapperImpl: TodoEventNotificationMapperImpl
    ): Mapper<TodoEventNotification, TodoEventNotificationDbModel>

    @Binds
    fun bindTodoItemDbModelMapperImpl(
        todoItemDbModelMapperImpl: TodoItemDbModelMapperImpl
    ): Mapper<TodoItem, TodoItemDbModel>

    companion object {
        @AppComponentScope
        @Provides
        fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }

        @AppComponentScope
        @Provides
        fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
            return appDatabase.todoItemDao()
        }

        @AppComponentScope
        @Provides
        fun provideToRemoveTodoItemDao(appDatabase: AppDatabase): ToRemoveTodoItemDao {
            return appDatabase.toRemoveTodoItemDao()
        }

        @AppComponentScope
        @Provides
        fun provideEventNotificationDao(appDatabase: AppDatabase): TodoEventNotificationDao {
            return appDatabase.eventNotificationDao()
        }
    }
}