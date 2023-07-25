package com.glebalekseevjk.todo.data.room.mapper

import com.glebalekseevjk.core.utils.Mapper
import com.glebalekseevjk.todo.data.room.model.TodoEventNotificationDbModel
import com.glebalekseevjk.todo.domain.entity.TodoEventNotification
import javax.inject.Inject

class TodoEventNotificationMapperImpl @Inject constructor() :
    Mapper<TodoEventNotification, TodoEventNotificationDbModel> {
    override fun mapItemToAnotherItem(item: TodoEventNotification): TodoEventNotificationDbModel =
        TodoEventNotificationDbModel(
            id = item.id,
            todoItemId = item.todoItemId,
            date = item.date,
        )

    override fun mapAnotherItemToItem(dbModel: TodoEventNotificationDbModel): TodoEventNotification =
        TodoEventNotification(
            id = dbModel.id,
            todoItemId = dbModel.todoItemId,
            date = dbModel.date,
        )
}