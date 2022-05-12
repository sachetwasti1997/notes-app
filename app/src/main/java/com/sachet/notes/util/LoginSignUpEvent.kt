package com.sachet.notes.util

sealed class LoginSignUpEvent {
    data class ErrorEvent(val message: String?, val actionMessage: String = ""): LoginSignUpEvent()
    data class SuccessEventSignUp(val message: String?, val actionMessage: String = ""): LoginSignUpEvent()
    data class SuccessEventLogIn(val message: String): LoginSignUpEvent()
}