package com.glebalekseevjk.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.domain.interactor.TodoItemUseCase
import com.glebalekseevjk.todoapp.presentation.activity.todoItemsRepositoryImpl
import com.glebalekseevjk.todoapp.utils.checkFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class TodoItemViewModel : ViewModel() {
    private val todoItemsUseCase = TodoItemUseCase(todoItemsRepositoryImpl)
    private val _todoItemState = MutableStateFlow<TodoItemState>(
        TodoItemState.Init(
            TodoItem(
                id = "0",
                text = "",
                importance = TodoItem.Companion.Importance.NORMAL,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = null
            )
        )
    )
    val todoItemState get(): StateFlow<TodoItemState> = _todoItemState

    fun dispatch(action: TodoItemAction) {
        when (action) {
            is TodoItemAction.Init -> init(action.todoId)
            TodoItemAction.DeleteTodoItem -> deleteTodoItem()
            TodoItemAction.SaveTodoItem -> saveTodoItem()
            is TodoItemAction.SetDeadline -> setDeadLine(action.deadline)
            is TodoItemAction.SetImportance -> setImportance(action.importance)
            is TodoItemAction.SetText -> setText(action.text)
        }
    }

    private fun init(todoId: String) {
        viewModelScope.launch {
            val todoItem = todoItemsUseCase.getTodoItemOrNull(todoId).checkFailure()
            when (val todoItemState = todoItemState.value) {
                is TodoItemState.Init -> {
                    if (todoItem != null) {
                        _todoItemState.emit(TodoItemState.Loaded(todoItem, true))
                    } else {
                        _todoItemState.emit(TodoItemState.Loaded(todoItemState.todoItem, false))

                    }
                }

                else -> throw RuntimeException("init when loaded")
            }
        }
    }

    private fun deleteTodoItem() {
        when (val todoItemState = todoItemState.value) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                if (!todoItemState.isEdit) return
                viewModelScope.launch {
                    todoItemsUseCase.deleteTodoItem(todoItemState.todoItem.id)
                }
            }
        }
    }

    private fun saveTodoItem() {
        val todoItemState = todoItemState.value
        when (todoItemState) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                viewModelScope.launch {
                    if (todoItemState.isEdit) {
                        todoItemsUseCase.update(todoItemState.todoItem)
                    } else {
                        todoItemsUseCase.add(todoItemState.todoItem)
                    }
                }
            }
        }
    }

    private fun setDeadLine(deadline: Date?) {
        when (val todoItemState = todoItemState.value) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                viewModelScope.launch {
                    _todoItemState.emit(
                        todoItemState.copy(
                            todoItem = todoItemState.todoItem.copy(deadline = deadline)
                        )
                    )
                }

            }
        }
    }

    private fun setImportance(importance: TodoItem.Companion.Importance) {
        when (val todoItemState = todoItemState.value) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                viewModelScope.launch {
                    _todoItemState.emit(
                        todoItemState.copy(
                            todoItem = todoItemState.todoItem.copy(importance = importance)
                        )
                    )
                }

            }
        }
    }

    private fun setText(text: String) {
        when (val todoItemState = todoItemState.value) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                viewModelScope.launch {
                    _todoItemState.emit(
                        todoItemState.copy(
                            todoItem = todoItemState.todoItem.copy(text = text)
                        )
                    )
                }

            }
        }
    }
}

sealed class TodoItemState(
    open val todoItem: TodoItem,
) {
    data class Init(
        override val todoItem: TodoItem
    ) : TodoItemState(todoItem)

    data class Loaded(
        override val todoItem: TodoItem,
        val isEdit: Boolean
    ) : TodoItemState(todoItem)
}

sealed class TodoItemAction {
    data class Init(val todoId: String) : TodoItemAction()
    object SaveTodoItem : TodoItemAction()
    object DeleteTodoItem : TodoItemAction()
    data class SetText(val text: String) : TodoItemAction()
    data class SetImportance(val importance: TodoItem.Companion.Importance) : TodoItemAction()
    data class SetDeadline(val deadline: Date?) : TodoItemAction()
}