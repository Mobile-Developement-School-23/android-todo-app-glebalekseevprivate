package com.glebalekseevjk.todoapp.domain.entity

import java.util.Calendar
import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date?,
    val isDone: Boolean,
    val createdAt: Date,
    val changedAt: Date?,
) {
    companion object {
        enum class Importance {
            LOW,
            NORMAL,
            IMPORTANT
        }
        val testTodoItems: MutableList<TodoItem> = mutableListOf(
            TodoItem(
                id = "1",
                text = "todo 1",
                importance = Importance.LOW,
                deadline = Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, 2) }.time,
                isDone = true,
                createdAt = Calendar.getInstance().time,
                changedAt = Calendar.getInstance().time,
            ),
            TodoItem(
                id = "2",
                text = "todo 2",
                importance = Importance.NORMAL,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "3",
                text = "todo 3",
                importance = Importance.IMPORTANT,
                deadline = Date(),
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),

            TodoItem(
                id = "4",
                text = "todo 4",
                importance = Importance.IMPORTANT,
                deadline = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 5) }.time,
                isDone = true,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "5",
                text = "todo 5",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "6",
                text = "todo 6",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "7",
                text = "todo 7",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "8",
                text = "todo 8",
                importance = Importance.LOW,
                deadline = null,
                isDone = true,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "9",
                text = "todo 9",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "10",
                text = "todo 10",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "11",
                text = "todo 11",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "12",
                text = "todo 12",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            ),
            TodoItem(
                id = "13",
                text = "todo 13",
                importance = Importance.LOW,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null,
            )
        )
    }
}