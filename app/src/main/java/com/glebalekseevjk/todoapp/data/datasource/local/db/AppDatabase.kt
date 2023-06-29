package com.glebalekseevjk.todoapp.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.TodoItemDbModel
import com.glebalekseevjk.todoapp.domain.entity.converter.DateConverter
import com.glebalekseevjk.todoapp.domain.entity.converter.ImportanceConverter

@Database(entities = [TodoItemDbModel::class, ToRemoveTodoItemDbModel::class], version = 1)
@TypeConverters(
    DateConverter::class,
    ImportanceConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao
    abstract fun toRemoveTodoItemDao(): ToRemoveTodoItemDao

    companion object {
        const val DATABASE_NAME = "todo-database"
    }
}