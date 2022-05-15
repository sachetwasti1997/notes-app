package com.sachet.notes.model

import com.sachet.notes.data.Note

data class RegisterState(
    var isSignUpPage: Boolean ?= false,
    var isLoginPage: Boolean ?= true,
    var isSearchStarted: Boolean = false
)
