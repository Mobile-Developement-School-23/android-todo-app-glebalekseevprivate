package com.glebalekseevjk.todo.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glebalekseevjk.todo.data.room.model.ToRemoveTodoItemDbModel

@Dao
interface ToRemoveTodoItemDao {
    @Query("SELECT * FROM ToRemoveTodoItemDbModel WHERE created_at <= :timestamp ORDER BY created_at ASC")
    fun getAllBeforeDate(timestamp: Long): List<ToRemoveTodoItemDbModel>

    @Query("SELECT * FROM ToRemoveTodoItemDbModel WHERE id = :id")
    fun getById(id: String): ToRemoveTodoItemDbModel?

    @Query("DELETE FROM ToRemoveTodoItemDbModel WHERE id = :id")
    fun deleteById(id: String)

    @Delete
    fun deleteList(vararg list: ToRemoveTodoItemDbModel)

    @Query("DELETE FROM ToRemoveTodoItemDbModel")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(toRemoveTodoItemDbModel: ToRemoveTodoItemDbModel)

}