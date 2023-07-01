package com.glebalekseevjk.todoapp.data.datasource.local.db.mapper

import com.glebalekseevjk.todoapp.data.datasource.local.db.model.TodoItemDbModel
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.utils.Mapper
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