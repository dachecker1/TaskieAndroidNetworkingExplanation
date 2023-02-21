package com.vk.dachecker.taskieandroidnetworkingexplanation.model.response

import com.squareup.moshi.Json
import com.vk.dachecker.taskieandroidnetworkingexplanation.model.Task

data class GetTasksResponse(@field:Json(name = "notes") val notes: List<Task> = listOf())