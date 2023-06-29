package com.glebalekseevjk.todoapp.ioc.module

import android.content.Context
import androidx.room.Room
import com.glebalekseevjk.todoapp.data.datasource.local.db.AppDatabase
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import dagger.Module
import dagger.Provides

@Module
interface LocalDataSourceModule {
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