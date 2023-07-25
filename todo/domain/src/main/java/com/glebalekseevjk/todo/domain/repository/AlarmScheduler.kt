package com.glebalekseevjk.todo.domain.repository

import java.util.Date

interface AlarmScheduler {
    fun schedule(packageContext: Class<*>, id: Int, date: Date)
    fun cancel(packageContext: Class<*>, id: Int)

    companion object {
        const val NOTIFICATION_ID = "notification_id"
    }
}