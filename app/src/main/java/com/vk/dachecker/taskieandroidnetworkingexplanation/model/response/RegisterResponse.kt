package com.vk.dachecker.taskieandroidnetworkingexplanation.model.response

import com.squareup.moshi.Json

data class RegisterResponse(@field:Json(name = "message")val message: String? = "")