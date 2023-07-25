package com.glebalekseevjk.todo.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.glebalekseevjk.auth.domain.usecase.PersonalUseCase
import com.glebalekseevjk.core.utils.Mapper
import com.glebalekseevjk.core.utils.di.ApplicationContext
import com.glebalekseevjk.core.utils.exception.ClientException
import com.glebalekseevjk.todo.data.retrofit.TodoItemService
import com.glebalekseevjk.todo.data.retrofit.model.TodoElement
import com.glebalekseevjk.todo.data.retrofit.model.TodoElementRequest
import com.glebalekseevjk.todo.data.retrofit.model.TodoElementResponse
import com.glebalekseevjk.todo.data.retrofit.model.TodoListRequest
import com.glebalekseevjk.todo.data.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todo.data.room.dao.TodoItemDao
import com.glebalekseevjk.todo.data.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todo.data.room.model.TodoItemDbModel
import com.glebalekseevjk.todo.data.utils.handleResponse
import com.glebalekseevjk.todo.data.utils.isInternetAvailable
import com.glebalekseevjk.todo.domain.entity.TodoItem
import com.glebalekseevjk.todo.domain.repository.TodoSynchronizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar
import javax.inject.Inject

/**
Этот класс необходим для синхронизации с удаленной версией данных
 */
class TodoSynchronizationRepositoryImpl @Inject constructor(
    private val personalUseCase: PersonalUseCase,
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
    private val todoItemService: TodoItemService,
    private val mapperTodoItemDbModel: Mapper<TodoItem, TodoItemDbModel>,
    private val mapperTodoElement: Mapper<TodoItem, TodoElement>,
    @ApplicationContext context: Context
) : TodoSynchronizationRepository {
    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val patchMutex = Mutex()

    private val pullMutex = Mutex()

    private val synchronizeMutex = Mutex()

    override val lastSyncDate get() = personalUseCase.lastSynchronizationDate

    override suspend fun pull() {
        withContext(Dispatchers.Default) {
            if (pullMutex.isLocked) return@withContext
            pullMutex.withLock {
                handleResponse(
                    onRequest = {
                        todoItemService.getAll()
                    },
                    onBadRequest = { patch() },
                    isInternetAvailable = { connectivityManager.isInternetAvailable() },
                    onSuccessful = {
                        it.body()?.let {
                            val list = it.list.map(::toDbModel)
                            todoItemDao.replaceAll(list)
                        }
                        updateLastSynchronizationDate()
                    }
                )
            }
        }
    }

    private suspend fun patch() {
        withContext(Dispatchers.Default) {
            if (patchMutex.isLocked) return@withContext
            val list = todoItemDao
                .getAll()
                .map { mapperTodoItemDbModel.mapAnotherItemToItem(it) }
                .map { mapperTodoElement.mapItemToAnotherItem(it) }
            patchMutex.withLock {
                handleResponse(
                    onRequest = {
                        todoItemService.updateAll(
                            TodoListRequest(
                                list
                            )
                        )
                    },
                    onBadRequest = { patch() },
                    isInternetAvailable = { connectivityManager.isInternetAvailable() },
                    onSuccessful = {
                        updateLastSynchronizationDate()
                    }
                )
            }
        }
    }

    private fun updateLastSynchronizationDate() {
        personalUseCase.lastSynchronizationDate = Calendar.getInstance().time
    }

    override suspend fun getSynchronizeState(): TodoSynchronizationRepository.SynchronizeState {
        return withContext(Dispatchers.Default) {
            return@withContext SynchronizeStateImpl()
        }
    }

    /**
    Этот класс представляет состояние синхронизации
     */
    inner class SynchronizeStateImpl : TodoSynchronizationRepository.SynchronizeState {
        private val toRemove: List<ToRemoveTodoItemDbModel>
        private val toUpdate: List<TodoItemDbModel>
        private val toAdd: List<TodoItemDbModel>
        override val isSynchronized: Boolean
            get() {
                return toRemove.isEmpty() && toUpdate.isEmpty() && toAdd.isEmpty()
            }

        init {
            val (_toRemove, _toUpdate, _toAdd) = getToRemoveToUpdateToAdd()
            toRemove = _toRemove
            toUpdate = _toUpdate
            toAdd = _toAdd
        }

        private fun getToRemoveToUpdateToAdd():
                Triple<List<ToRemoveTodoItemDbModel>, List<TodoItemDbModel>, List<TodoItemDbModel>> {
            val lastSyncDate = personalUseCase.lastSynchronizationDate
            val listChangedAfterDate = todoItemDao.getAll().filter { it.changedAt >= lastSyncDate }
            val toRemove = toRemoveTodoItemDao.getAllBeforeDate(lastSyncDate.time)
            val toUpdate = listChangedAfterDate.filter { it.createdAt < lastSyncDate }
            val toAdd = listChangedAfterDate.filter { it.createdAt >= lastSyncDate }
            return Triple(
                toRemove,
                toUpdate,
                toAdd,
            )
        }

        override suspend fun synchronize() {
            if (isSynchronized) return
            if (synchronizeMutex.isLocked) return

            withContext(Dispatchers.Default) {
                synchronizeMutex.withLock {
                    for (item in toRemove) actionRemote(
                        onRequested = { todoItemService.deleteById(item.id) },
                    )
                    for (item in toUpdate.map(::toElement)) actionRemote(
                        onRequested = {
                            todoItemService.updateById(
                                item.id!!,
                                TodoElementRequest(item)
                            )
                        },
                    )
                    for (item in toAdd.map(::toElement)) actionRemote(
                        onRequested = {
                            todoItemService.insert(
                                TodoElementRequest(
                                    item
                                )
                            )
                        },
                        onNotFound = { throw ClientException() }
                    )
                    updateLastSynchronizationDate()
                    toRemoveTodoItemDao.deleteList(*toRemove.toTypedArray())
                }
            }
        }

        private suspend fun actionRemote(
            onRequested: suspend () -> Response<TodoElementResponse>,
            onNotFound: () -> Unit = {},
        ) {
            handleResponse(
                onRequest = onRequested,
                onBadRequest = { patch() },
                isInternetAvailable = { connectivityManager.isInternetAvailable() },
                onNotFound = onNotFound
            )
        }
    }

    private fun toDbModel(todoElement: TodoElement): TodoItemDbModel {
        return todoElement.let { mapperTodoElement.mapAnotherItemToItem(it) }
            .let { mapperTodoItemDbModel.mapItemToAnotherItem(it) }
    }

    private fun toElement(todoItemDbModel: TodoItemDbModel): TodoElement {
        return todoItemDbModel.let { mapperTodoItemDbModel.mapAnotherItemToItem(it) }
            .let { mapperTodoElement.mapItemToAnotherItem(it) }
    }
}