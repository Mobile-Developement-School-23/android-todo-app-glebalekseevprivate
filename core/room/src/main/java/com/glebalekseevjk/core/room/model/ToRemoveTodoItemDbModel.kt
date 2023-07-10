package com.glebalekseevjk.core.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ToRemoveTodoItemDbModel(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
)