package com.glebalekseevjk.todo.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.glebalekseevjk.todo.data.room.model.TodoItemDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {
    @Query("SELECT * FROM TodoItemDbModel WHERE id = :id")
    fun getById(id: String): TodoItemDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(todoItemDbModel: TodoItemDbModel)

    @Query("DELETE FROM TodoItemDbModel WHERE id = :id")
    fun deleteById(id: String)

    @Query("SELECT * FROM TodoItemDbModel ORDER BY created_at ASC")
    fun getAllAsFlow(): Flow<List<TodoItemDbModel>>

    @Query("SELECT * FROM TodoItemDbModel ORDER BY created_at ASC")
    fun getAll(): List<TodoItemDbModel>

    @Query("DELETE FROM TodoItemDbModel")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceAll(vararg list: TodoItemDbModel)

    @Transaction
    fun replaceAll(list: List<TodoItemDbModel>) {
        deleteAll()
        insertOrReplaceAll(*list.toTypedArray())
    }
}