package com.sachet.notes.util

data class LoginResponse(
    val token: String ?= null,
    val exception: String ?= null,
    val code: Int ?= null
)