package com.glebalekseevjk.core.retrofit.mapper

import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.preferences.PersonalStorage
import com.glebalekseevjk.core.retrofit.model.TodoElement
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
Ответственность класса TodoElementMapperImpl:
Класс отвечает за преобразование объектов типа TodoItem
и TodoElement друг в друга. Он содержит два метода:
mapItemToAnotherItem для преобразования TodoItem в TodoElement
и mapAnotherItemToItem для преобразования TodoElement в TodoItem.
Класс также использует PersonalSharedPreferences для получения
информации о последнем обновлении на устройстве.
 */
class TodoElementMapperImpl @Inject constructor(
    private val personalStorage: PersonalStorage
) : Mapper<TodoItem, TodoElement> {
    override fun mapItemToAnotherItem(item: TodoItem): TodoElement {
        return TodoElement(
            id = item.id,
            text = item.text,
            importance = item.importance.toString().lowercase(),
            done = item.isDone,
            deadline = item.deadline?.time?.let { (it / DATE_CORRECT_VALUE).toInt() },
            color = COLOR_MOCK,
            lastUpdatedBy = personalStorage.deviceId,
            changedAt = item.changedAt.time.let { (it / DATE_CORRECT_VALUE).toInt() },
            createdAt = item.createdAt.time.let { (it / DATE_CORRECT_VALUE).toInt() },
        )
    }

    override fun mapAnotherItemToItem(anotherItem: TodoElement): TodoItem {
        return TodoItem(
            id = anotherItem.id!!,
            text = anotherItem.text!!,
            importance = TodoItem.Companion.Importance.valueOf(
                anotherItem.importance!!.uppercase(Locale.getDefault())
            ),
            deadline = anotherItem.deadline?.let { Date(it.toLong() * DATE_CORRECT_VALUE) },
            isDone = anotherItem.done!!,
            createdAt = anotherItem.createdAt!!.let { Date(it.toLong() * DATE_CORRECT_VALUE) },
            changedAt = anotherItem.changedAt!!.let { Date(it.toLong() * DATE_CORRECT_VALUE) }
        )
    }

    companion object {
        private const val DATE_CORRECT_VALUE = 1000
        private const val COLOR_MOCK = "#FFFFFF"
    }
}