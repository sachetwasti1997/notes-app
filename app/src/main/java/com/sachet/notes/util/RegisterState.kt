package com.sachet.notes.util

import com.sachet.notes.data.Note

data class RegisterState(
    var token: String ?= null,
    var isSignUpPage: Boolean ?= false,
    var isLoginPage: Boolean ?= true,
    var ex: String ?= null,
    var noteList: List<Note> ?= null,
    var isTokenExtracted: Boolean = false
)
