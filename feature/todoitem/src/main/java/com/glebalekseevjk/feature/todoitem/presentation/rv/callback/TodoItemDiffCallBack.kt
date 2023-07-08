package com.glebalekseevjk.feature.todoitem.presentation.rv.callback

import androidx.recyclerview.widget.DiffUtil
import com.glebalekseevjk.todoapp.domain.entity.entity.TodoItem

class TodoItemDiffCallBack : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}