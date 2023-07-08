package com.glebalekseevjk.feature.todoitem.presentation.rv.callback

import androidx.recyclerview.widget.DiffUtil
import com.glebalekseevjk.domain.todoitem.entity.TodoItem

/**
Ответственность класса TodoItemDiffCallBack:
Класс TodoItemDiffCallBack отвечает за сравнение элементов списка TodoItem
для определения их изменений. Он реализует интерфейс DiffUtil.ItemCallback
и предоставляет логику сравнения двух элементов списка.
Его задача - определить, являются ли два элемента одинаковыми или имеют различия в содержимом.
 */
class TodoItemDiffCallBack : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}