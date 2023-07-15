package com.glebalekseevjk.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class EventNotificationDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "todo_id") val todoId: String,
    @ColumnInfo(name = "date") val date: Date,
)