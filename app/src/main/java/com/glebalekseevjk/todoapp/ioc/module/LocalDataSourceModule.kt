package com.glebalekseevjk.todoapp.ioc.module

import android.content.Context
import androidx.room.Room
import com.glebalekseevjk.todoapp.data.datasource.local.db.AppDatabase
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.mapper.TodoItemDbModelMapperImpl
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.TodoItemDbModel
import com.glebalekseevjk.todoapp.data.datasource.remote.mapper.TodoElementMapperImpl
import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoElement
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import com.glebalekseevjk.todoapp.utils.Mapper
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface LocalDataSourceModule {
    @AppComponentScope
    @Binds
    fun bindTodoElementMapperImpl(todoElementMapperImpl: TodoElementMapperImpl): Mapper<TodoItem, TodoElement>

    @AppComponentScope
    @Binds
    fun bindTodoItemDbModelMapperImpl(todoItemDbModelMapperImpl: TodoItemDbModelMapperImpl): Mapper<TodoItem, TodoItemDbModel>

    companion object {
        @AppComponentScope
        @Provides
        fun provideAppDataBase(context: Context): AppDatabase {
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
    }
}