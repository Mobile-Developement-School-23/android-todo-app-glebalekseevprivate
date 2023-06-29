package com.glebalekseevjk.todoapp.data.datasource.remote

import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoElement
import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoElementResponse
import com.glebalekseevjk.todoapp.data.datasource.remote.model.TodoListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoItemService {
    @GET("list")
    suspend fun getAll(): Response<TodoListResponse>

    @PATCH("list")
    fun updateAll(
        @Body list: List<TodoElement>
    ): Response<TodoListResponse>

    @GET("list/{id}")
    suspend fun getById(
        @Path("id") id: String,
    ): Response<TodoElementResponse>

    @POST("list")
    suspend fun insert(
        @Body element: TodoElement
    ): Response<TodoElementResponse>

    @PUT("list/{id}")
    suspend fun updateById(
        @Path("id") id: String,
        @Body element: TodoElement
    ): Response<TodoElementResponse>

    @DELETE("list/{id}")
    fun deleteById(
        @Path("id") id: String,
    ): Response<TodoElementResponse>
}