package com.glebalekseevjk.todoapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.todoapp.Constants.SYNCHRONIZATION_TIMEOUT
import com.glebalekseevjk.todoapp.data.repository.AuthRepository
import com.glebalekseevjk.todoapp.data.repository.SynchronizationRepository
import com.glebalekseevjk.todoapp.data.repository.TodoItemRepository
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.domain.entity.exception.AuthorizationException
import com.glebalekseevjk.todoapp.domain.entity.exception.ClientException
import com.glebalekseevjk.todoapp.domain.entity.exception.ConnectionException
import com.glebalekseevjk.todoapp.domain.entity.exception.ServerException
import com.glebalekseevjk.todoapp.domain.entity.exception.UnknownException
import com.glebalekseevjk.todoapp.worker.SchedulerManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class TodoItemsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val synchronizationRepository: SynchronizationRepository,
    private val todoItemRepository: TodoItemRepository,
    private val schedulerManager: SchedulerManager
) : ViewModel() {
    private val _todoItemsState = MutableStateFlow<TodoItemsState>(TodoItemsState.Init)
    val todoItemsState get(): StateFlow<TodoItemsState> = _todoItemsState

    val notificationChannel = Channel<NotificationType>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            stopLoading()
        }
        when (throwable) {
            is AuthorizationException -> {
                notificationChannel.trySend(NotificationType.Unauthorized)
            }

            is ClientException -> {
                notificationChannel.trySend(NotificationType.Client)
            }

            is ServerException -> {
                notificationChannel.trySend(NotificationType.Server)
            }

            is ConnectionException -> {
                schedulerManager.setupOneTimeSynchronize()
                notificationChannel.trySend(NotificationType.Connection)
            }

            is UnknownException -> {
                notificationChannel.trySend(NotificationType.Unknown)
            }

            else -> throw throwable
        }
    }

    private suspend fun stopLoading() {
        when (val state = _todoItemsState.value) {
            is TodoItemsState.Loading -> {
                _todoItemsState.emit(
                    TodoItemsState.Loaded(
                        state.visibility,
                        state.todoItemsDisplay,
                        state.todoItems,
                        state.countDone
                    )
                )
            }

            else -> {}
        }
    }

    init {
        viewModelScope
            .withExceptionHandler()
            .withDefaultDispatcher()
            .launch {
                while (true) {
                    delay(SYNCHRONIZATION_TIMEOUT)
                    synchronize()
                }
            }
    }

    private suspend fun synchronize() {
        synchronizationRepository.getSynchronizeState().synchronize()
    }

    override fun onCleared() {
        super.onCleared()
        schedulerManager.setupOneTimeSynchronize()
    }

    fun dispatch(action: TodoItemsAction) {
        when (action) {
            TodoItemsAction.Init -> init()
            TodoItemsAction.ChangeVisibility -> changeVisibility()
            is TodoItemsAction.ChangeDoneStatus -> changeDoneStatus(action.todoId)
            is TodoItemsAction.DeleteTodoItem -> deleteTodoItem(action.todoId)
            is TodoItemsAction.SetDoneStatus -> setDoneStatus(action.todoId)
            TodoItemsAction.Quit -> quit()
            TodoItemsAction.PullToRefresh -> pullToRefresh()
        }
    }

    private fun init() {
        if (_todoItemsState.value != TodoItemsState.Init) return
        viewModelScope.launch {
            _todoItemsState.emit(TodoItemsState.Loading(true, emptyList(), emptyList(), 0))
            withExceptionHandler()
                .withDefaultDispatcher()
                .withSupervisorJob()
                .launch {
                    synchronize()
                }.join()

            withExceptionHandler()
                .withDefaultDispatcher()
                .withSupervisorJob()
                .launch {
                    if (synchronizationRepository.getSynchronizeState().isSynchronized && todoItemRepository.todoItems.first()
                            .isEmpty()
                    ) {
                        pull()
                    }
                }.join()

            todoItemRepository.todoItems.collect { todoItems ->
                when (val state = todoItemsState.value) {
                    is TodoItemsState.Loading -> {
                        _todoItemsState.emit(
                            TodoItemsState.Loaded(
                                visibility = state.visibility,
                                todoItems = todoItems,
                                todoItemsDisplay = if (state.visibility) todoItems else todoItems.filter { !it.isDone },
                                countDone = todoItems.filter { it.isDone }.size,
                            )
                        )
                    }

                    is TodoItemsState.Loaded -> {
                        _todoItemsState.emit(
                            state.copy(
                                todoItems = todoItems,
                                todoItemsDisplay = if (state.visibility) todoItems else todoItems.filter { !it.isDone },
                                countDone = todoItems.filter { it.isDone }.size,
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
                            visibility = !state.visibility,
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


    private fun changeDoneStatus(todoId: String) {
        viewModelScope.launch {
            todoItemRepository.changeDoneStatus(todoId)
        }
    }

    private fun deleteTodoItem(todoId: String) {
        viewModelScope.launch {
            todoItemRepository.deleteTodoItem(todoId)
        }
    }

    private fun setDoneStatus(todoId: String) {
        viewModelScope.launch {
            todoItemRepository.setDoneStatus(todoId)
        }
    }

    private fun quit() {
        viewModelScope.launch {
            authRepository.quit()
        }
    }

    private val pullToRefreshMutex = Mutex()
    private fun pullToRefresh() {
        viewModelScope
            .withExceptionHandler()
            .withDefaultDispatcher()
            .launch {
                val newState = when (val state = _todoItemsState.value) {
                    TodoItemsState.Init -> TodoItemsState.Loading(
                        false,
                        emptyList(),
                        emptyList(),
                        0
                    )

                    is TodoItemsState.Loaded -> TodoItemsState.Loading(
                        state.visibility,
                        state.todoItemsDisplay,
                        state.todoItems,
                        state.countDone
                    )

                    is TodoItemsState.Loading -> TodoItemsState.Loading(
                        state.visibility,
                        state.todoItemsDisplay,
                        state.todoItems,
                        state.countDone
                    )
                }
                _todoItemsState.emit(newState)

                if (pullToRefreshMutex.isLocked) {
                    stopLoading()
                    return@launch
                }

                pullToRefreshMutex.withLock {
                    synchronize()
                    pull()
                    stopLoading()
                }
            }
    }

    private suspend fun pull() {
        synchronizationRepository.pull()
    }

    private fun CoroutineScope.withExceptionHandler(): CoroutineScope =
        CoroutineScope(exceptionHandler + this.coroutineContext)

    private fun CoroutineScope.withDefaultDispatcher(): CoroutineScope =
        CoroutineScope(Dispatchers.Default + this.coroutineContext)

    private fun CoroutineScope.withSupervisorJob(): CoroutineScope =
        CoroutineScope(this.coroutineContext + SupervisorJob())
}

sealed class TodoItemsState(
    open val visibility: Boolean
) {
    object Init : TodoItemsState(false)
    data class Loading(
        override val visibility: Boolean,
        val todoItemsDisplay: List<TodoItem>,
        val todoItems: List<TodoItem>,
        val countDone: Int,
    ) : TodoItemsState(visibility)

    data class Loaded(
        override val visibility: Boolean,
        val todoItemsDisplay: List<TodoItem>,
        val todoItems: List<TodoItem>,
        val countDone: Int,
    ) : TodoItemsState(visibility)
}

sealed class TodoItemsAction {
    object Init : TodoItemsAction()
    object ChangeVisibility : TodoItemsAction()
    data class ChangeDoneStatus(val todoId: String, val context: Context) : TodoItemsAction()
    data class DeleteTodoItem(val todoId: String, val context: Context) : TodoItemsAction()
    data class SetDoneStatus(val todoId: String, val context: Context) : TodoItemsAction()
    object PullToRefresh : TodoItemsAction()
    object Quit : TodoItemsAction()
}

sealed class NotificationType {
    object Unknown : NotificationType()
    object Unauthorized : NotificationType()
    object Client : NotificationType()
    object Server : NotificationType()
    object Connection : NotificationType()
}
