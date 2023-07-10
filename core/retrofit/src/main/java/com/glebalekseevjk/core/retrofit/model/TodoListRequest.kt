package com.glebalekseevjk.core.retrofit.model

import com.google.gson.annotations.SerializedName

data class TodoListRequest(
    @SerializedName("list") val list: List<TodoElement>,
)