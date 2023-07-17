package com.glebalekseevjk.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebalekseevjk.core.room.converter.DateConverter
import com.glebalekseevjk.core.room.converter.ImportanceConverter
import com.glebalekseevjk.core.room.dao.EventNotificationDao
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.model.EventNotificationDbModel
import com.glebalekseevjk.core.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.core.room.model.TodoItemDbModel

@Database(
    entities = [
        TodoItemDbModel::class,
        ToRemoveTodoItemDbModel::class,
        EventNotificationDbModel::class
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
    abstract fun eventNotificationDao(): EventNotificationDao

    companion object {
        const val DATABASE_NAME = "todo-database"
    }
}