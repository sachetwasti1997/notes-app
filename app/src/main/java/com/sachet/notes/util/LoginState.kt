package com.sachet.notes.util

data class LoginState(
    var userName: String = "",
    var password: String = "",
    var passwordVisibility: Boolean = false
)
