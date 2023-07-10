package com.glebalekseevjk.core.room.converter

import androidx.room.TypeConverter
import com.glebalekseevjk.domain.todoitem.entity.TodoItem.Companion.Importance

/**
Ответственность класса ImportanceConverter:
Класс ImportanceConverter отвечает за преобразование значения типа
Importance в строку и обратно с использованием аннотаций @TypeConverter.
Его главная ответственность заключается в конвертации значения приоритета
Importance в строку и обратно, чтобы облегчить сохранение
и загрузку данных в базе данных или других хранилищах данных.
 */
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