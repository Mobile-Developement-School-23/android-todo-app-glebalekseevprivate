package com.glebalekseevjk.todoapp.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class TodoElementResponse(
    @SerializedName("revision") val revision: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("element") val element: TodoElement,
)