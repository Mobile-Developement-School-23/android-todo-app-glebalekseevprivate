package com.glebalekseevjk.core.room.converter

import androidx.room.TypeConverter
import java.util.Date

/**
Ответственность класса DateConverter:
Преобразование значений типа Long в объекты типа Date и обратно.
Класс отвечает за конвертацию даты и времени между двумя разными типами данных.
 */
class DateConverter {
    @TypeConverter
    fun toDate(value: Long): Date? {
        if (value == -1L) return null
        return Date(value)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long {
        date ?: return -1
        return date.time
    }
}