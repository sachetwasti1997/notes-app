package com.sachet.notes.util

import com.sachet.notes.data.Note
import com.sachet.notes.data.NotesOrder

sealed class NotesEvent{
    data class Order(val newNotesOrder: NotesOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data class FailureEvent(val message: String): NotesEvent()
    data class TokenExpiredFailure(val message: String): NotesEvent()
    object RestoreNotes: NotesEvent()
    object ToggleOrderSelection: NotesEvent()
}
