package com.glebalekseevjk.todo.todolist.presentation.rv.callback

import androidx.recyclerview.widget.DiffUtil
import com.glebalekseevjk.todo.domain.entity.TodoItem

/**
 Этот класс нужен для того, чтобы DiffUtil мог сравнивать элементы списка
 */
class TodoItemDiffCallBack : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}