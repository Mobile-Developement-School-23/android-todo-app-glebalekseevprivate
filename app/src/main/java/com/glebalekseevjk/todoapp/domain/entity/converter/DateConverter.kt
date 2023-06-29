package com.glebalekseevjk.todoapp.domain.entity.converter

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {
    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}