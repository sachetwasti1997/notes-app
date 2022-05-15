package com.sachet.notes.model

data class NotesTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)