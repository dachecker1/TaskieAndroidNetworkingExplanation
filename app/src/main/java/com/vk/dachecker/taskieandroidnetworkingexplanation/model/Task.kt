package com.vk.dachecker.taskieandroidnetworkingexplanation.model

import com.squareup.moshi.Json

class Task(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "content") val content: String,
    @field:Json(name = "isCompleted") val isCompleted: Boolean,
    @field:Json(name = "taskPriority") val taskPriority: Int
)