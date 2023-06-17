package com.glebalekseevjk.todoapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.domain.interactor.TodoItemUseCase
import com.glebalekseevjk.todoapp.presentation.activity.todoItemsRepositoryImpl
import com.glebalekseevjk.todoapp.utils.checkFailure
import com.glebalekseevjk.todoapp.utils.printFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoItemsViewModel : ViewModel() {
    private val todoItemsUseCase = TodoItemUseCase(todoItemsRepositoryImpl)
    private val _todoItemsState = MutableStateFlow<TodoItemsState>(TodoItemsState.Init)
    val todoItemsState get(): StateFlow<TodoItemsState> = _todoItemsState

    fun dispatch(action: TodoItemsAction) {
        when (action) {
            TodoItemsAction.Init -> init()
            TodoItemsAction.ChangeVisibility -> changeVisibility()
            is TodoItemsAction.ChangeDoneStatus -> changeDoneStatus(action.todoId, action.context)
            is TodoItemsAction.DeleteTodoItem -> deleteTodoItem(action.todoId, action.context)
            is TodoItemsAction.SetDoneStatus -> setDoneStatus(action.todoId, action.context)
        }
    }

    private fun init() {
        viewModelScope.launch {
            _todoItemsState.emit(TodoItemsState.Loading(true))
            todoItemsUseCase.getTodoItems().collect {
                val todoItems = it.checkFailure()
                when (val state = todoItemsState.value) {
                    is TodoItemsState.Loading -> {
                        _todoItemsState.emit(
                            TodoItemsState.Loaded(
                                visibility = state.visibility,
                                todoItems = todoItems,
                                todoItemsDisplay = if (state.visibility) todoItems else todoItems.filter { !it.isDone },
                            )
                        )
                    }

                    is TodoItemsState.Loaded -> {
                        _todoItemsState.emit(
                            state.copy(
                                todoItems = todoItems,
                                todoItemsDisplay = if (state.visibility) todoItems else todoItems.filter { !it.isDone },
                            )
                        )
                    }

                    is TodoItemsState.Init -> {}
                }
            }
        }
    }

    private fun changeVisibility() {
        if (todoItemsState.value is TodoItemsState.Init) return
        when (val state = todoItemsState.value) {
            is TodoItemsState.Init -> return
            is TodoItemsState.Loading -> {
                viewModelScope.launch {
                    _todoItemsState.emit(
                        state.copy(
                            visibility = !state.visibility
                        )
                    )
                }
            }

            is TodoItemsState.Loaded -> {
                viewModelScope.launch {
                    _todoItemsState.emit(
                        state.copy(
                            visibility = !state.visibility,
                            todoItemsDisplay = if (!state.visibility) state.todoItems else state.todoItems.filter { !it.isDone }
                        )
                    )
                }
            }
        }
    }

    private fun changeDoneStatus(todoId: String, context: Context){
        viewModelScope.launch {
            todoItemsUseCase.changeDoneStatus(todoId).printFailure(context)
        }
    }

    private fun deleteTodoItem(todoId: String, context: Context){
        viewModelScope.launch {
            todoItemsUseCase.deleteTodoItem(todoId).printFailure(context)
        }
    }


    private fun setDoneStatus(todoId: String, context: Context){
        viewModelScope.launch {
            todoItemsUseCase.setDoneStatus(todoId).printFailure(context)
        }
    }
}

sealed class TodoItemsState(
    open val visibility: Boolean
) {
    object Init : TodoItemsState(false)
    data class Loading(
        override val visibility: Boolean,
    ) : TodoItemsState(visibility)

    data class Loaded(
        override val visibility: Boolean,
        val todoItems: List<TodoItem>,
        val todoItemsDisplay: List<TodoItem>,
    ) : TodoItemsState(visibility)
}

sealed class TodoItemsAction {
    object Init : TodoItemsAction()
    object ChangeVisibility : TodoItemsAction()
    data class ChangeDoneStatus(val todoId: String, val context: Context) : TodoItemsAction()
    data class DeleteTodoItem(val todoId: String, val context: Context) : TodoItemsAction()
    data class SetDoneStatus(val todoId: String, val context: Context) : TodoItemsAction()
}