package com.glebalekseevjk.todo.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class TodoElementRequest(
    @SerializedName("element")
    val element: TodoElement,
)