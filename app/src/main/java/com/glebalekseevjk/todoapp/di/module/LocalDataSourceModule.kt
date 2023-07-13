package com.glebalekseevjk.todoapp.di.module//package com.glebalekseevjk.todoapp.ioc_old.module

import android.content.Context
import androidx.room.Room
import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.retrofit.mapper.TodoElementMapperImpl
import com.glebalekseevjk.core.retrofit.model.TodoElement
import com.glebalekseevjk.core.room.AppDatabase
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.mapper.TodoItemDbModelMapperImpl
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface LocalDataSourceModule {
    @Binds
    fun bindTodoElementMapperImpl(
        todoElementMapperImpl: TodoElementMapperImpl
    ): Mapper<TodoItem, TodoElement>

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
    }
}