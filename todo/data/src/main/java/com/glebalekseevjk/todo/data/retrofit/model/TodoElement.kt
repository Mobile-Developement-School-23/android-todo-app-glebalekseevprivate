package com.glebalekseevjk.todo.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class TodoElement(
    @SerializedName("id")
    val id: String?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("importance")
    val importance: String?,
    @SerializedName("done")
    val done: Boolean?,
    @SerializedName("deadline")
    val deadline: Long?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String?,
    @SerializedName("changed_at")
    val changedAt: Long?,
    @SerializedName("created_at")
    val createdAt: Long?,
)