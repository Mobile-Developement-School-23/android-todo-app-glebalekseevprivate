package com.glebalekseevjk.todoapp.domain.entity.converter

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {
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