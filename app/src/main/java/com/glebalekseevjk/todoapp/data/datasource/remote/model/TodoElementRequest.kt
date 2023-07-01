package com.glebalekseevjk.todoapp.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class TodoElementRequest(
    @SerializedName("element") val element: TodoElement,
)