package com.sachet.notes.util

data class RegisterState(
    var token: String ?= null,
    var isSignUpPage: Boolean ?= false,
    var isLoginPage: Boolean ?= true,
    var ex: String ?= null
)
