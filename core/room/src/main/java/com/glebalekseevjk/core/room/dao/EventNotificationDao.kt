package com.glebalekseevjk.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glebalekseevjk.core.room.model.EventNotificationDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EventNotificationDao {
    @Query("SELECT * FROM EventNotificationDbModel WHERE id = :eventNotificationId")
    fun get(eventNotificationId: Int): EventNotificationDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(eventNotificationDbModel: EventNotificationDbModel)

    @Query("DELETE FROM EventNotificationDbModel WHERE id = :eventNotificationId")
    fun delete(eventNotificationId: Int)

    @Query("DELETE FROM EventNotificationDbModel WHERE todo_id = :todoId")
    fun deleteForTodo(todoId: String)

    @Query("SELECT * FROM EventNotificationDbModel WHERE todo_id = :todoId ORDER BY date ASC")
    fun getForTodo(todoId: String): List<EventNotificationDbModel>

    @Query("SELECT * FROM EventNotificationDbModel ORDER BY date ASC")
    fun getAllAsFlow(): Flow<List<EventNotificationDbModel>>

    @Query("SELECT * FROM EventNotificationDbModel ORDER BY date ASC")
    fun getAll(): List<EventNotificationDbModel>

//    @Query("DELETE FROM EventNotificationDbModel WHERE todo_id = :todoId AND is_done = 0 AND date >= :date")
//    suspend fun deleteLaterThanDateInclusiveWhereIsDoneFalseForTodo(todoId: Int, date: Long)
//
//    @Query("SELECT * FROM EventNotificationDbModel WHERE todo_id = :todoId AND is_done = 0 AND date >= :date")
//    suspend fun getLaterThanDateInclusiveWhereIsDoneFalseForTodo(
//        todoId: Int,
//        date: Long
//    ): List<EventNotificationDbModel>
//
//    @Query("SELECT * FROM EventNotificationDbModel WHERE todo_id = :todoId AND date = :date")
//    suspend fun getForTodoAndDate(todoId: Int, date: Long): EventNotificationDbModel?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAll(vararg eventNotificationDbModel: EventNotificationDbModel)



//    @Query("SELECT * FROM EventNotificationDbModel ORDER BY date ASC")
//    fun getAll(): List<EventNotificationDbModel>
}