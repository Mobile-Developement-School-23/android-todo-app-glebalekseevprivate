package com.glebalekseevjk.core.retrofit.model

import com.google.gson.annotations.SerializedName

data class TodoElementResponse(
    @SerializedName("revision") val revision: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("element") val element: TodoElement,
)