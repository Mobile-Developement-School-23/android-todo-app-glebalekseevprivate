package com.glebalekseevjk.todoapp.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.dao.TodoItemDao
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.TodoItemDbModel
import com.glebalekseevjk.todoapp.data.datasource.preferences.PersonalSharedPreferences
import com.glebalekseevjk.todoapp.data.datasource.remote.TodoItemService
import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoElement
import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoElementRequest
import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoListRequest
import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoListResponse
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import com.glebalekseevjk.todoapp.utils.Mapper
import com.glebalekseevjk.todoapp.utils.handleResponse
import com.glebalekseevjk.todoapp.utils.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@AppComponentScope
class SynchronizationRepository @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences,
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
    private val todoItemService: TodoItemService,
    private val mapperTodoItemDbModel: Mapper<TodoItem, TodoItemDbModel>,
    private val mapperTodoElement: Mapper<TodoItem, TodoElement>,
    context: Context
) {
    private val connectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    private val patchMutex = Mutex()

    private val pullMutex = Mutex()

    private val synchronizeMutex = Mutex()

    suspend fun pull() {
        withContext(Dispatchers.Default) {
            if (pullMutex.isLocked) return@withContext
            pullMutex.withLock {
                handleResponse<TodoListResponse>(
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
                handleResponse<TodoListResponse>(
                    onRequest = {
                        todoItemService.updateAll(TodoListRequest(list))
                    },
                    onBadRequest = { patch() },
                    isInternetAvailable = { connectivityManager.isInternetAvailable() },
                    onSuccessful = {

                    }
                )
            }
        }
    }

    suspend fun getSynchronizeState(): SynchronizeState {
        return withContext(Dispatchers.Default) {
            return@withContext SynchronizeState()
        }
    }

    inner class SynchronizeState {
        private val toRemove: List<ToRemoveTodoItemDbModel>
        private val toUpdate: List<TodoItemDbModel>
        private val toAdd: List<TodoItemDbModel>
        val isSynchronized: Boolean
            get() {
                return toRemove.isEmpty() && toUpdate.isEmpty() && toAdd.isEmpty()
            }

        init {
            val (_toRemove, _toUpdate, _toAdd) = getToRemoveToUpdateToAdd()
            toRemove = _toRemove
            toUpdate = _toUpdate
            toAdd = _toAdd
        }

        private fun getToRemoveToUpdateToAdd(): Triple<List<ToRemoveTodoItemDbModel>, List<TodoItemDbModel>, List<TodoItemDbModel>> {
            val lastSyncDate = personalSharedPreferences.lastSynchronizationDate
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

        suspend fun synchronize() {
            if (isSynchronized) return
            if (synchronizeMutex.isLocked) return

            withContext(Dispatchers.Default) {
                synchronizeMutex.withLock {
                    for (item in toRemove) {
                        handleResponse(
                            onRequest = {
                                todoItemService.deleteById(item.id)
                            },
                            onBadRequest = { patch() },
                            isInternetAvailable = { connectivityManager.isInternetAvailable() },
                            onNotFound = {

                            }
                        )
                    }
                    for (item in toUpdate.map(::toElement)) {
                        handleResponse(
                            onRequest = {
                                todoItemService.updateById(item.id!!, TodoElementRequest(item))
                            },
                            onBadRequest = { patch() },
                            isInternetAvailable = { connectivityManager.isInternetAvailable() },
                            onNotFound = {

                            })
                    }
                    for (item in toAdd.map(::toElement)) {
                        handleResponse(
                            onRequest = {
                                todoItemService.insert(TodoElementRequest(item))
                            },
                            onBadRequest = { patch() },
                            isInternetAvailable = { connectivityManager.isInternetAvailable() })
                    }
                    personalSharedPreferences.lastSynchronizationDate = Calendar.getInstance().time
                    toRemoveTodoItemDao.deleteList(*toRemove.toTypedArray())
                }
            }
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