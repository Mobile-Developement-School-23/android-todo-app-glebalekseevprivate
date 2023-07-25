package com.glebalekseevjk.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebalekseevjk.todo.data.room.converter.DateConverter
import com.glebalekseevjk.todo.data.room.converter.ImportanceConverter
import com.glebalekseevjk.todo.data.room.dao.TodoEventNotificationDao
import com.glebalekseevjk.todo.data.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todo.data.room.dao.TodoItemDao
import com.glebalekseevjk.todo.data.room.model.TodoEventNotificationDbModel
import com.glebalekseevjk.todo.data.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todo.data.room.model.TodoItemDbModel

@Database(
    entities = [
        TodoItemDbModel::class,
        ToRemoveTodoItemDbModel::class,
        TodoEventNotificationDbModel::class
    ],
    version = 1
)
@TypeConverters(
    DateConverter::class,
    ImportanceConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao
    abstract fun toRemoveTodoItemDao(): ToRemoveTodoItemDao
    abstract fun eventNotificationDao(): TodoEventNotificationDao

    companion object {
        const val DATABASE_NAME = "todo-database"
    }
}