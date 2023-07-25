package com.glebalekseevjk.todo.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TodoEventNotificationDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "todo_item_id")
    val todoItemId: String,
    @ColumnInfo(name = "date")
    val date: Date,
)