package com.sachet.notes.data

data class Note constructor(
    val noteId: String?= null,
    val title: String ?= null,
    val description: String ?= null,
    val userId: String = "user1",
    val localDateTime: String ?= null
)
