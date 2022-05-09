package com.sachet.notes.network

import android.util.JsonToken
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

    suspend fun saveNotes(token: String, note: Note): Response<Note, Boolean, Exception>{
        val noteSaved = Response<Note, Boolean, Exception>(null, true, null)
        try {
            noteSaved.data = notesApi.saveNote(token, note)
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
        token: String?,
    ): ArrayList<Note>{
//        var noteList = ArrayList<Note>()
//        try {
            return notesApi.getNotesOfUser(token)
//        }catch (ex: CancellationException){
////            Log.d("Notes", "saveNotes: $ex")
//        }catch (ex: Exception){
////            Log.d("Notes", "saveNotes: $ex")
//        }
//        return noteList
    }

    suspend fun getNoteById(
        token: String?,
        noteId: String
    ): Note?{
        var note: Note? = null
        println("$token $noteId")
        try {
               note = notesApi.getNoteById(token, noteId)
        }catch (ex: CancellationException){
            println(ex.message)
//            Log.d("Notes", "saveNotes: $ex")
        }catch (ex: Exception){
            println(ex.message)
//            Log.d("Notes", "saveNotes: $ex")
        }
        return note
    }

    suspend fun deleteNote(token: String?, noteId: String?): Response<String, Boolean, Exception>{
        val result = Response<String, Boolean, Exception>(null, true, null)
        try {
            result.data = notesApi.deleteNoteById(token, noteId)
        }catch (ex: CancellationException){
            println("DELETE ${ex.message}")
            result.exception = ex
        }catch (ex: Exception){
            println("DELETE ${ex.message}")
            result.exception = ex
        }
        return result
    }


}