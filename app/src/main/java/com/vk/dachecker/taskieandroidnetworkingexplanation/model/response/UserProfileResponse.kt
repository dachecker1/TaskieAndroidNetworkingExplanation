package com.vk.dachecker.taskieandroidnetworkingexplanation.model.response

import com.squareup.moshi.Json

class UserProfileResponse(
    @field:Json(name = "email") val email: String?,
    @field:Json(name = "name") val name: String?,
)