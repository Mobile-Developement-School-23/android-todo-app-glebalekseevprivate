package com.glebalekseevjk.feature.todoitem.presentation.viewmodel


interface ITodoItemViewModel {
    val viewStates: TodoItemState
    fun dispatch(action: TodoItemAction)
}