package com.glebalekseevjk.feature.todoitem.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.domain.todoitem.repository.TodoItemRepository
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
Ответственность класса TodoItemViewModel:
Управление состоянием и взаимодействие с репозиторием для работы с элементами списка задач.
Класс отвечает за обработку действий пользователя,
изменение состояния элемента списка задач и его сохранение/удаление в репозитории.
 */
class TodoItemViewModel @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) : ViewModel(), ITodoItemViewModel {
    override var viewStates by mutableStateOf<TodoItemState>(
        TodoItemState.Init(
            TodoItem(
                id = "0",
                text = "",
                importance = TodoItem.Companion.Importance.BASIC,
                deadline = null,
                isDone = false,
                createdAt = Calendar.getInstance().time,
                changedAt = Calendar.getInstance().time
            )
        )
    )
        private set

    override fun dispatch(action: TodoItemAction) {
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
            val todoItem = todoItemRepository.getTodoItemByIdOrNull(todoId)
            when (val todoItemState = viewStates) {
                is TodoItemState.Init -> {
                    if (todoItem != null) {
                        viewStates = TodoItemState.Loaded(todoItem, true)
                    } else {
                        viewStates = TodoItemState.Loaded(todoItemState.todoItem, false)
                    }
                }

                else -> throw RuntimeException("init when loaded")
            }
        }
    }

    private fun deleteTodoItem() {
        when (val todoItemState = viewStates) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                if (!todoItemState.isEdit) return
                CoroutineScope(Dispatchers.IO).launch {
                    todoItemRepository.deleteTodoItem(todoItemState.todoItem.id)
                }
            }
        }
    }

    private fun saveTodoItem() {
        when (val todoItemState = viewStates) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (todoItemState.isEdit) {
                        todoItemRepository.updateTodoItem(todoItemState.todoItem)
                    } else {
                        todoItemRepository.addTodoItem(todoItemState.todoItem)
                    }
                }
            }
        }
    }

    private fun setDeadLine(deadline: Date?) {
        when (val todoItemState = viewStates) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                viewModelScope.launch {
                    viewStates =
                        todoItemState.copy(
                            todoItem = todoItemState.todoItem.copy(deadline = deadline)
                        )
                }
            }
        }
    }

    private fun setImportance(importance: TodoItem.Companion.Importance) {
        when (val todoItemState = viewStates) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                viewModelScope.launch {
                    viewStates =
                        todoItemState.copy(
                            todoItem = todoItemState.todoItem.copy(importance = importance)
                        )
                }
            }
        }
    }

    private fun setText(text: String) {
        when (val todoItemState = viewStates) {
            is TodoItemState.Init -> return
            is TodoItemState.Loaded -> {
                viewModelScope.launch {
                    viewStates =
                        todoItemState.copy(
                            todoItem = todoItemState.todoItem.copy(text = text)
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