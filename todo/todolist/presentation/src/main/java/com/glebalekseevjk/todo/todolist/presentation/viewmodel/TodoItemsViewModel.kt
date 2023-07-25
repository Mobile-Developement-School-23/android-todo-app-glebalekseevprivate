package com.glebalekseevjk.todo.todolist.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.auth.domain.entity.NightMode
import com.glebalekseevjk.auth.domain.usecase.AuthUseCase
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.Constants.SYNCHRONIZATION_TIMEOUT
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.exception.AuthorizationException
import com.glebalekseevjk.core.utils.exception.ClientException
import com.glebalekseevjk.core.utils.exception.ConnectionException
import com.glebalekseevjk.core.utils.exception.ServerException
import com.glebalekseevjk.core.utils.exception.UnknownException
import com.glebalekseevjk.todo.domain.entity.TodoItem
import com.glebalekseevjk.todo.domain.repository.TodoItemRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationRepository
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationSchedulerManager
import com.glebalekseevjk.todo.todolist.presentation.viewmodel.TodoItemsState.Init
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 Этот класс отвечает за отображение списка задач, а также за синхронизацию задач с сервером.
 */
class TodoItemsViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val synchronizationRepository: TodoSynchronizationRepository,
    private val todoItemRepository: TodoItemRepository,
    private val synchronizationSchedulerManager: TodoSynchronizationSchedulerManager,
    private val personalUseCase: PersonalUseCase,
    @ApplicationContext context: Context
) : ViewModel() {
    private val resources = context.resources
    private val lastSyncDate
        get() = SimpleDateFormat(
            resources.getString(com.glebalekseevjk.design.R.string.date_pattern_2),
            Locale("ru")
        )
            .format(synchronizationRepository.lastSyncDate)

    private val _todoItemsState =
        MutableStateFlow<TodoItemsState>(Init(lastSyncDate))

    private val _modalBottomSheetState =
        MutableStateFlow<ModalBottomSheetState>(ModalBottomSheetState.Hidden)

    val todoItemsState get(): StateFlow<TodoItemsState> = _todoItemsState
    val modalBottomSheetState get(): StateFlow<ModalBottomSheetState> = _modalBottomSheetState

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
                synchronizationSchedulerManager.setupOneTimeSynchronize()
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
                        lastSyncDate,
                        state.todoItemsDisplay,
                        state.todoItems,
                        state.countDone,
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

    private suspend fun synchronize(isAlreadySynchronized: suspend () -> Unit = {}) {
        val state = synchronizationRepository.getSynchronizeState()
        val isSync = state.isSynchronized
        if (isSync) isAlreadySynchronized.invoke()
        else state.synchronize()
    }

    override fun onCleared() {
        super.onCleared()
        synchronizationSchedulerManager.setupOneTimeSynchronize()
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

            TodoItemsAction.HideModalBottomSheet -> hideModalBottomSheet()
            is TodoItemsAction.SaveNightMode -> saveNightMode(action.beforeSave)
            is TodoItemsAction.SetNightMode -> setNightMode(action.nightMode)
            TodoItemsAction.ShowSettingsModalBottomSheet -> showSettingsModalBottomSheet()
        }
    }

    private fun init() {
        if (_todoItemsState.value !is Init) return
        viewModelScope.launch {
            _todoItemsState.emit(
                TodoItemsState.Loading(
                    true,
                    _todoItemsState.value.lastSyncDate,
                    emptyList(),
                    emptyList(),
                    0
                )
            )
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
                    if (synchronizationRepository.getSynchronizeState().isSynchronized
                        && todoItemRepository.todoItems.first()
                            .isEmpty()
                    ) {
                        pull()
                    }
                }.join()

            todoItemRepository.todoItems.collectLatest { todoItems ->
                when (val state = todoItemsState.value) {
                    is TodoItemsState.Loading -> {
                        _todoItemsState.emit(
                            TodoItemsState.Loaded(
                                visibility = state.visibility,
                                lastSyncDate = lastSyncDate,
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
                                lastSyncDate = lastSyncDate,
                                todoItemsDisplay = if (state.visibility) todoItems else todoItems.filter { !it.isDone },
                                countDone = todoItems.filter { it.isDone }.size,
                            )
                        )
                    }

                    is Init -> {}
                }
            }
        }
    }

    private fun changeVisibility() {
        if (todoItemsState.value is Init) return
        when (val state = todoItemsState.value) {
            is Init -> return
            is TodoItemsState.Loading -> {
                viewModelScope.launch {
                    _todoItemsState.emit(
                        state.copy(
                            lastSyncDate = lastSyncDate,
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
                            lastSyncDate = lastSyncDate,
                            todoItemsDisplay = if (!state.visibility) state.todoItems
                            else state.todoItems.filter { !it.isDone }
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
            todoItemRepository.delete(todoId)
        }
    }

    private fun setDoneStatus(todoId: String) {
        viewModelScope.launch {
            todoItemRepository.setDoneStatus(todoId)
        }
    }

    private fun quit() {
        viewModelScope.launch {
            authUseCase.quit()
        }
    }

    private val pullToRefreshMutex = Mutex()
    private fun pullToRefresh() {
        viewModelScope
            .withExceptionHandler()
            .withDefaultDispatcher()
            .launch {
                val newState = when (val state = _todoItemsState.value) {
                    is Init -> TodoItemsState.Loading(
                        state.visibility,
                        state.lastSyncDate,
                        emptyList(),
                        emptyList(),
                        0
                    )

                    is TodoItemsState.Loaded -> TodoItemsState.Loading(
                        state.visibility,
                        state.lastSyncDate,
                        state.todoItemsDisplay,
                        state.todoItems,
                        state.countDone
                    )

                    is TodoItemsState.Loading -> TodoItemsState.Loading(
                        state.visibility,
                        state.lastSyncDate,
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
                    synchronize(
                        isAlreadySynchronized = {
                            pull()
                        }
                    )
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

    private fun hideModalBottomSheet() {
        _modalBottomSheetState.tryEmit(ModalBottomSheetState.Hidden)
    }

    private fun saveNightMode(beforeSave: () -> Unit) {
        viewModelScope.launch {
            val state = modalBottomSheetState.value
            beforeSave.invoke()
            when (state) {
                ModalBottomSheetState.Hidden -> return@launch
                is ModalBottomSheetState.ShownSettings -> personalUseCase.setNightMode(state.nightMode)
            }
        }
    }

    private fun setNightMode(nightMode: NightMode) {
        viewModelScope.launch {
            _modalBottomSheetState.emit(
                ModalBottomSheetState.ShownSettings(
                    nightMode
                )
            )
        }
    }

    private fun showSettingsModalBottomSheet() {
        viewModelScope.launch {
            _modalBottomSheetState.emit(
                ModalBottomSheetState.ShownSettings(
                    personalUseCase.nightMode
                )
            )
        }
    }
}

sealed class ModalBottomSheetState {
    object Hidden : ModalBottomSheetState()
    data class ShownSettings(
        val nightMode: NightMode
    ) : ModalBottomSheetState()
}

sealed class TodoItemsState(
    open val visibility: Boolean,
    open val lastSyncDate: String,
) {
    class Init(lastSyncDate: String) : TodoItemsState(false, lastSyncDate)
    data class Loading(
        override val visibility: Boolean,
        override val lastSyncDate: String,
        val todoItemsDisplay: List<TodoItem>,
        val todoItems: List<TodoItem>,
        val countDone: Int,
    ) : TodoItemsState(visibility, lastSyncDate)

    data class Loaded(
        override val visibility: Boolean,
        override val lastSyncDate: String,
        val todoItemsDisplay: List<TodoItem>,
        val todoItems: List<TodoItem>,
        val countDone: Int,
    ) : TodoItemsState(visibility, lastSyncDate)
}

sealed class TodoItemsAction {
    object Init : TodoItemsAction()
    object ChangeVisibility : TodoItemsAction()
    data class ChangeDoneStatus(val todoId: String, val context: Context) : TodoItemsAction()
    data class DeleteTodoItem(val todoId: String, val context: Context) : TodoItemsAction()
    data class SetDoneStatus(val todoId: String, val context: Context) : TodoItemsAction()
    object PullToRefresh : TodoItemsAction()
    object Quit : TodoItemsAction()
    object ShowSettingsModalBottomSheet : TodoItemsAction()
    object HideModalBottomSheet : TodoItemsAction()
    data class SetNightMode(val nightMode: NightMode) : TodoItemsAction()
    data class SaveNightMode(val beforeSave: () -> Unit) : TodoItemsAction()
}

sealed class NotificationType {
    object Unknown : NotificationType()
    object Unauthorized : NotificationType()
    object Client : NotificationType()
    object Server : NotificationType()
    object Connection : NotificationType()
}
