package com.sachet.notes.model

import com.sachet.notes.data.Note

data class NoteState(
    var notes: List<Note> = ArrayList(),
    var notesOrder: NotesOrder = NotesOrder.Date(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false,
    val credential: String? = "",
    val isSearchStarted: Boolean = false,
)
