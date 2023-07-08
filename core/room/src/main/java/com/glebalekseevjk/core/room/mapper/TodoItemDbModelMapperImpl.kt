package com.glebalekseevjk.core.room.mapper

import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import javax.inject.Inject

class TodoItemDbModelMapperImpl @Inject constructor() : Mapper<TodoItem, TodoItemDbModel> {
    override fun mapItemToAnotherItem(item: TodoItem): TodoItemDbModel {
        return TodoItemDbModel(
            id = item.id,
            text = item.text,
            importance = item.importance,
            deadline = item.deadline,
            isDone = item.isDone,
            createdAt = item.createdAt,
            changedAt = item.changedAt

        )
    }

    override fun mapAnotherItemToItem(anotherItem: TodoItemDbModel): TodoItem {
        return TodoItem(
            id = anotherItem.id,
            text = anotherItem.text,
            importance = anotherItem.importance,
            deadline = anotherItem.deadline,
            isDone = anotherItem.isDone,
            createdAt = anotherItem.createdAt,
            changedAt = anotherItem.changedAt
        )
    }
}