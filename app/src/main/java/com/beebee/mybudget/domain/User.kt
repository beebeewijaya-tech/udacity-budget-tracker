package com.beebee.mybudget.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class User(
    val email: String? = "",
    val token: String? = ""
)

@JsonClass(generateAdapter = true)
data class AuthUser(
    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String
)