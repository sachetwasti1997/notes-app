package com.sachet.notes.data

data class LoginResponse(
    val token: String ?= null,
    val exception: String ?= null,
    val code: Int ?= null
)