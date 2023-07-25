package com.glebalekseevjk.todo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glebalekseevjk.todo.data.room.model.TodoEventNotificationDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoEventNotificationDao {
    @Query("SELECT * FROM TodoEventNotificationDbModel WHERE id = :eventNotificationId")
    fun get(eventNotificationId: Int): TodoEventNotificationDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todoEventNotificationDbModel: TodoEventNotificationDbModel)

    @Query("DELETE FROM TodoEventNotificationDbModel WHERE id = :eventNotificationId")
    fun delete(eventNotificationId: Int)

    @Query("DELETE FROM TodoEventNotificationDbModel WHERE todo_item_id = :todoId")
    fun deleteForTodo(todoId: String)

    @Query("SELECT * FROM TodoEventNotificationDbModel WHERE todo_item_id = :todoId ORDER BY date ASC")
    fun getForTodo(todoId: String): List<TodoEventNotificationDbModel>

    @Query("SELECT * FROM TodoEventNotificationDbModel ORDER BY date ASC")
    fun getAllAsFlow(): Flow<List<TodoEventNotificationDbModel>>

    @Query("SELECT * FROM TodoEventNotificationDbModel ORDER BY date ASC")
    fun getAll(): List<TodoEventNotificationDbModel>
}