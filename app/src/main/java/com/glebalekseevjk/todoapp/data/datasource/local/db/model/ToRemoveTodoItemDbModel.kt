package com.glebalekseevjk.todoapp.data.datasource.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ToRemoveTodoItemDbModel(
    @PrimaryKey @ColumnInfo(name = "todoId") val todoId: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
)