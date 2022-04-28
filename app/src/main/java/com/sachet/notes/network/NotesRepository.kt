package com.sachet.notes.network

import android.util.Log
import com.sachet.notes.data.Note
import com.sachet.notes.data.Response
import java.lang.Exception
import java.util.concurrent.CancellationException
import javax.inject.Inject

class NotesRepository
@Inject constructor(
    private val notesApi: NotesApi
){

    suspend fun saveNotes(note: Note): Response<Note, Boolean, Exception>{
        val noteSaved = Response<Note, Boolean, Exception>(null, true, null)
        try {
            noteSaved.data = notesApi.saveNote(note)
            noteSaved.loading = false
        }catch (ex: CancellationException){
//            Log.d("Notes", "saveNotes: $ex")
            throw ex
        }catch (ex: Exception){
//            Log.d("Notes", "saveNotes: $ex")
            noteSaved.loading = false
            noteSaved.exception = ex
        }
        return noteSaved
    }

    suspend fun getAllNotes(
        userId: String,
    ): ArrayList<Note>{
        var noteList = ArrayList<Note>()
        try {
            noteList = notesApi.getNotesOfUser(userId)
        }catch (ex: CancellationException){
//            Log.d("Notes", "saveNotes: $ex")
        }catch (ex: Exception){
//            Log.d("Notes", "saveNotes: $ex")
        }
        return noteList
    }

    suspend fun getNoteById(
        noteId: String
    ): Note?{
        var note: Note? = null
        try {
               note = notesApi.getNoteById(noteId)
        }catch (ex: CancellationException){
//            Log.d("Notes", "saveNotes: $ex")
        }catch (ex: Exception){
//            Log.d("Notes", "saveNotes: $ex")
        }
        return note
    }

    suspend fun deleteNote(noteId: String): Response<String, Boolean, Exception>{
        val result = Response<String, Boolean, Exception>(null, true, null)
        try {
            result.data = notesApi.deleteNoteById(noteId)
        }catch (ex: CancellationException){
            result.exception = ex
        }catch (ex: Exception){
            result.exception = ex
        }
        return result
    }


}