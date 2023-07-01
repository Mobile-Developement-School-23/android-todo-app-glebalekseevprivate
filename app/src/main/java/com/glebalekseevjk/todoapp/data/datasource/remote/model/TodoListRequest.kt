package com.glebalekseevjk.todoapp.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class TodoListRequest(
    @SerializedName("list") val list: List<TodoElement>,
)