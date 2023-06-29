package com.glebalekseevjk.todoapp.domain.entity.converter

import androidx.room.TypeConverter
import com.glebalekseevjk.todoapp.domain.entity.TodoItem.Companion.Importance

object ImportanceConverter {
    @TypeConverter
    fun fromPriority(importance: Importance): String {
        return importance.name
    }

    @TypeConverter
    fun toPriority(importance: String): Importance {
        return Importance.valueOf(importance)
    }
}