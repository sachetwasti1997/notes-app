package com.sachet.notes.util

import com.sachet.notes.data.Note

sealed class NotesEvent{
    data class Order(val newNotesOrder: NotesOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    object RestoreNotes: NotesEvent()
    object ToggleOrderSelection: NotesEvent()
}
