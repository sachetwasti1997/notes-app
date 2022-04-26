package com.sachet.notes.util

import com.sachet.notes.data.Note

data class NoteState(
    var notes: List<Note> = ArrayList(),
    var notesOrder: NotesOrder = NotesOrder.Date(OrderType.Ascending),
    val isOrderSectionVisible: Boolean = false
)