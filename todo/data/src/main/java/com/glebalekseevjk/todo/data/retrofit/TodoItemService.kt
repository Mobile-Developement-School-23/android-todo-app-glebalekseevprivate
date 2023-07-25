package com.glebalekseevjk.todo.data.retrofit

import com.glebalekseevjk.todo.data.retrofit.model.TodoElementRequest
import com.glebalekseevjk.todo.data.retrofit.model.TodoElementResponse
import com.glebalekseevjk.todo.data.retrofit.model.TodoListRequest
import com.glebalekseevjk.todo.data.retrofit.model.TodoListResponse
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
    suspend fun updateAll(
        @Body list: TodoListRequest
    ): Response<TodoListResponse>

    @GET("list/{id}")
    suspend fun getById(
        @Path("id") id: String,
    ): Response<TodoElementResponse>

    @POST("list")
    suspend fun insert(
        @Body element: TodoElementRequest
    ): Response<TodoElementResponse>

    @PUT("list/{id}")
    suspend fun updateById(
        @Path("id") id: String,
        @Body element: TodoElementRequest
    ): Response<TodoElementResponse>

    @DELETE("list/{id}")
    suspend fun deleteById(
        @Path("id") id: String,
    ): Response<TodoElementResponse>
}