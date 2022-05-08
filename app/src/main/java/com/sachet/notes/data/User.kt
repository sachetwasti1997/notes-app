package com.sachet.notes.data

data class User(
    var userId: String ?= null,
    var firstName: String ?= null,
    var lastName: String ?= null,
    var email: String ?= null,
    var userName: String ?= null,
    var password: String ?= null,
    var roles: List<String> = listOf("ROLE_USER")
)
