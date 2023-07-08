package com.glebalekseevjk.core.room.converter

import androidx.room.TypeConverter
import com.glebalekseevjk.todoapp.domain.entity.entity.TodoItem.Companion.Importance

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