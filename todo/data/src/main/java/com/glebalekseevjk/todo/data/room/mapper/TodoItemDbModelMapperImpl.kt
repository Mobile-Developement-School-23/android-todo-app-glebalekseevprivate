package com.glebalekseevjk.todo.data.room.mapper

import com.glebalekseevjk.core.utils.Mapper
import com.glebalekseevjk.todo.data.room.model.TodoItemDbModel
import com.glebalekseevjk.todo.domain.entity.TodoItem
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