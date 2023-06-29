package com.glebalekseevjk.todoapp.data.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.ToRemoveTodoItemDbModel
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.TodoItemDbModel
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

@Dao
interface ToRemoveTodoItemDao {
    @Query("SELECT * FROM ToRemoveTodoItemDbModel WHERE created_at <= :timestamp ORDER BY created_at ASC")
    fun getAllBeforeDate(timestamp: Long): List<ToRemoveTodoItemDbModel>

    @Delete
    fun deleteList(vararg list: ToRemoveTodoItemDbModel)

    @Query("DELETE FROM ToRemoveTodoItemDbModel")
    fun deleteAll()
}