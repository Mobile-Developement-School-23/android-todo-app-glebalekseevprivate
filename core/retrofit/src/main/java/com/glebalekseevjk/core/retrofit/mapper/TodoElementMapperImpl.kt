package com.glebalekseevjk.core.retrofit.mapper

import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.preferences.PersonalSharedPreferences
import com.glebalekseevjk.todoapp.domain.entity.entity.TodoItem
import com.glebalekseevjk.core.retrofit.model.TodoElement
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TodoElementMapperImpl @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences
) : Mapper<TodoItem, TodoElement> {
    override fun mapItemToAnotherItem(item: TodoItem): TodoElement {
        return TodoElement(
            id = item.id,
            text = item.text,
            importance = item.importance.toString().lowercase(),
            done = item.isDone,
            deadline = item.deadline?.time?.let { (it / 1000).toInt() },
            color = "#FFFFFF",
            lastUpdatedBy = personalSharedPreferences.deviceId,
            changedAt = item.changedAt.time.let { (it / 1000).toInt() },
            createdAt = item.createdAt.time.let { (it / 1000).toInt() },
        )
    }

    override fun mapAnotherItemToItem(anotherItem: TodoElement): TodoItem {
        return TodoItem(
            id = anotherItem.id!!,
            text = anotherItem.text!!,
            importance = TodoItem.Companion.Importance.valueOf(
                anotherItem.importance!!.uppercase(Locale.getDefault())
            ),
            deadline = anotherItem.deadline?.let { Date(it.toLong() * 1000) },
            isDone = anotherItem.done!!,
            createdAt = anotherItem.createdAt!!.let { Date(it.toLong() * 1000) },
            changedAt = anotherItem.changedAt!!.let { Date(it.toLong() * 1000) }
        )
    }
}