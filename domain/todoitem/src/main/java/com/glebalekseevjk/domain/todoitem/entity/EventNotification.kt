package com.glebalekseevjk.domain.todoitem.entity

import java.util.Date


data class EventNotification(
    val id: Int = UNDEFINED_ID,
    val todoId: String,
    val date: Date,
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
