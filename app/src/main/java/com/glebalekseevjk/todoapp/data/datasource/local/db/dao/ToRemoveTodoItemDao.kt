package com.glebalekseevjk.todoapp.data.datasource.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glebalekseevjk.todoapp.data.datasource.local.db.model.ToRemoveTodoItemDbModel

@Dao
interface ToRemoveTodoItemDao {
    @Query("SELECT * FROM ToRemoveTodoItemDbModel WHERE created_at <= :timestamp ORDER BY created_at ASC")
    fun getAllBeforeDate(timestamp: Long): List<ToRemoveTodoItemDbModel>

    @Delete
    fun deleteList(vararg list: ToRemoveTodoItemDbModel)

    @Query("DELETE FROM ToRemoveTodoItemDbModel")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(toRemoveTodoItemDbModel: ToRemoveTodoItemDbModel)

}