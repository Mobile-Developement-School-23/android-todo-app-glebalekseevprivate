package com.glebalekseevjk.todo.data.room.converter

import androidx.room.TypeConverter
import com.glebalekseevjk.todo.domain.entity.TodoItem.Companion.Importance


class ImportanceConverter {
    @TypeConverter
    fun fromPriority(importance: Importance): String {
        return importance.name
    }

    @TypeConverter
    fun toPriority(importance: String): Importance {
        return Importance.valueOf(importance)
    }
}