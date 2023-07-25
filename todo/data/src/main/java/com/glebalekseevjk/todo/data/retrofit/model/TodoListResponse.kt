package com.glebalekseevjk.todo.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class TodoListResponse(
    @SerializedName("revision")
    val revision: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("list")
    val list: List<TodoElement>,
)