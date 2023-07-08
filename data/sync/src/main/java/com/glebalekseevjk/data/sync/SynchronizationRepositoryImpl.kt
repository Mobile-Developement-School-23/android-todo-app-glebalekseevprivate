package com.glebalekseevjk.data.sync

import android.content.Context
import android.net.ConnectivityManager
import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.preferences.PersonalSharedPreferences
import com.glebalekseevjk.core.retrofit.TodoItemService
import com.glebalekseevjk.core.retrofit.model.TodoElement
import com.glebalekseevjk.core.retrofit.model.TodoElementRequest
import com.glebalekseevjk.core.retrofit.model.TodoElementResponse
import com.glebalekseevjk.core.retrofit.model.TodoListRequest
import com.glebalekseevjk.core.room.dao.ToRemoveTodoItemDao
import com.glebalekseevjk.core.room.dao.TodoItemDao
import com.glebalekseevjk.core.room.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.core.room.model.TodoItemDbModel
import com.glebalekseevjk.data.sync.utils.handleResponse
import com.glebalekseevjk.data.sync.utils.isInternetAvailable
import com.glebalekseevjk.design.R
import com.glebalekseevjk.domain.sync.SynchronizationRepository
import com.glebalekseevjk.domain.todoitem.entity.TodoItem
import com.glebalekseevjk.domain.todoitem.exception.ClientException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

/**
Ответственность класса SynchronizationRepositoryImpl:
Класс отвечает за синхронизацию данных между локальным хранилищем
и удаленным сервисом. Он обеспечивает обновление, получение
и отправку данных, а также контролирует состояние синхронизации.
 */
class SynchronizationRepositoryImpl @Inject constructor(
    private val personalSharedPreferences: PersonalSharedPreferences,
    private val todoItemDao: TodoItemDao,
    private val toRemoveTodoItemDao: ToRemoveTodoItemDao,
    private val todoItemService: TodoItemService,
    private val mapperTodoItemDbModel: Mapper<TodoItem, TodoItemDbModel>,
    private val mapperTodoElement: Mapper<TodoItem, TodoElement>,
    context: Context
) : SynchronizationRepository {
    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val patchMutex = Mutex()

    private val pullMutex = Mutex()

    private val synchronizeMutex = Mutex()

    private val formatter =
        SimpleDateFormat(context.getString(R.string.date_pattern_2), Locale("ru"))

    override val lastSyncDate: String
        get() = personalSharedPreferences.lastSynchronizationDate.let {
            formatter.format(
                it
            )
        }

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

                    }
                )
            }
        }
    }

    override suspend fun getSynchronizeState(): SynchronizeStateImpl {
        return withContext(Dispatchers.Default) {
            return@withContext SynchronizeStateImpl()
        }
    }

    /**
    Ответственность класса SynchronizeStateImpl:
    Управление синхронизацией состояния элементов.
    Класс отвечает за определение элементов, требующих удаления,
    обновления и добавления в базу данных.
    Также класс выполняет синхронизацию с удаленным сервером,
    отправляя запросы на удаление, обновление и добавление элементов.
    Он также отслеживает время последней синхронизации
    и обновляет его после завершения синхронизации.
     */
    inner class SynchronizeStateImpl : SynchronizationRepository.SynchronizeState {
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
                    personalSharedPreferences.lastSynchronizationDate = Calendar.getInstance().time
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