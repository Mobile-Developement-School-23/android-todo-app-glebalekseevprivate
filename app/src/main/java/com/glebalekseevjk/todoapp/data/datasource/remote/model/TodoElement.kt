package com.glebalekseevjk.todoapp.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class TodoElement(
    @SerializedName("id") val id: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("importance") val importance: String?,
    @SerializedName("done") val done: Boolean?,
    @SerializedName("deadline") val deadline: Int?,
    @SerializedName("color") val color: String?,
    @SerializedName("last_updated_by") val last_updated_by: String?,
    @SerializedName("changed_at") val changedAt: Int?,
    @SerializedName("created_at") val createdAt: Int?,
)