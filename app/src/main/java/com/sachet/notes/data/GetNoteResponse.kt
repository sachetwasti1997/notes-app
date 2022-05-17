package com.sachet.notes.data

import com.sachet.notes.data.Note

data class GetNoteResponse(
    val notes: List<Note> = emptyList(),
    val message: String,
    val code: Int
)
