package com.glebalekseevjk.todo.domain.entity

import java.util.Date


data class TodoEventNotification(
    val id: Int = UNDEFINED_ID,
    val todoItemId: String,
    val date: Date,
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
