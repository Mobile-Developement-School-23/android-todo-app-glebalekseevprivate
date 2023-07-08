package com.glebalekseevjk.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glebalekseevjk.todoapp.domain.entity.entity.TodoItem.Companion.Importance
import java.util.Date

@Entity
data class TodoItemDbModel(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "importance") val importance: Importance,
    @ColumnInfo(name = "deadline") val deadline: Date?,
    @ColumnInfo(name = "is_done") val isDone: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "changed_at") val changedAt: Date,
)