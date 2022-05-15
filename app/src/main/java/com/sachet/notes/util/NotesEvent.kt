package com.sachet.notes.util

import com.sachet.notes.data.Note
import com.sachet.notes.model.NotesOrder

sealed class NotesEvent{
    data class Order(val newNotesOrder: NotesOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data class FetchNotesEventFailure(val message: String): NotesEvent()
    data class TokenExpiredFailure(val message: String = "Token Expired, please log in again to continue"): NotesEvent()
    object RestoreNotes: NotesEvent()
    object ToggleOrderSelection: NotesEvent()
}
