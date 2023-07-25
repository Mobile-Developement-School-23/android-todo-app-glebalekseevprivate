package com.glebalekseevjk.todo.data.retrofit.mapper

import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.Mapper
import com.glebalekseevjk.todo.data.retrofit.model.TodoElement
import com.glebalekseevjk.todo.domain.entity.TodoItem
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class TodoElementMapperImpl @Inject constructor(
    private val personalUseCase: PersonalUseCase
) : Mapper<TodoItem, TodoElement> {
    override fun mapItemToAnotherItem(item: TodoItem): TodoElement {
        return TodoElement(
            id = item.id,
            text = item.text,
            importance = item.importance.toString().lowercase(),
            done = item.isDone,
            deadline = item.deadline?.time?.let { (it / DATE_CORRECT_VALUE) },
            color = COLOR_MOCK,
            lastUpdatedBy = personalUseCase.deviceId,
            changedAt = item.changedAt.time.let { (it / DATE_CORRECT_VALUE) },
            createdAt = item.createdAt.time.let { (it / DATE_CORRECT_VALUE) },
        )
    }

    override fun mapAnotherItemToItem(anotherItem: TodoElement): TodoItem {
        return TodoItem(
            id = anotherItem.id!!,
            text = anotherItem.text!!,
            importance = TodoItem.Companion.Importance.valueOf(
                anotherItem.importance!!.uppercase(Locale.getDefault())
            ),
            deadline = anotherItem.deadline?.let { Date(it * DATE_CORRECT_VALUE) },
            isDone = anotherItem.done!!,
            createdAt = anotherItem.createdAt!!.let { Date(it * DATE_CORRECT_VALUE) },
            changedAt = anotherItem.changedAt!!.let { Date(it * DATE_CORRECT_VALUE) }
        )
    }

    companion object {
        private const val DATE_CORRECT_VALUE = 1
        private const val COLOR_MOCK = "#FFFFFF"
    }
}