package com.sachet.notes.data

data class LoginRequest(
    var userName: String ?= null,
    var password: String ?= null
)
