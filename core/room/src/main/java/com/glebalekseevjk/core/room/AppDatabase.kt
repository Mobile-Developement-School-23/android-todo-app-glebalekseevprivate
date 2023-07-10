package com.glebalekseevjk.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.core.room.converter.DateConverter
import com.glebalekseevjk.core.room.converter.ImportanceConverter

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