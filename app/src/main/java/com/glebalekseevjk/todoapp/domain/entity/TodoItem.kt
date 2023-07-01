package com.glebalekseevjk.todoapp.domain.entity

import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date?,
    val isDone: Boolean,
    val createdAt: Date,
    val changedAt: Date,
) {
    companion object {
        enum class Importance {
            LOW,
            BASIC,
            IMPORTANT
        }
    }
}