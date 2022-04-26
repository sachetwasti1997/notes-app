package com.sachet.notes.data

data class Note constructor(
    val noteId: String?= null,
    val title: String ,
    val description: String ,
    val userId: String = "user1",
    val localDateTime: String ,
    val color: Int
)
