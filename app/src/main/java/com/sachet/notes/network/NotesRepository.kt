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
//            Log.d("NotesRepository", "saveNotes: $noteSaved")
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

    suspend fun getAllNotes(userId: String): Response<ArrayList<Note>, Boolean, Exception>{
        val noteList = Response<ArrayList<Note>, Boolean, Exception>(null, true, null)
        try {
            noteList.data = notesApi.getNotesOfUser(userId)
            Log.d("NotesRepository", "getNotes: $noteList")
            noteList.loading = false
        }catch (ex: CancellationException){
            Log.d("Notes", "saveNotes: $ex")
            noteList.loading = false
            noteList.exception = ex
        }catch (ex: Exception){
            Log.d("Notes", "saveNotes: $ex")
            noteList.loading = false
            noteList.exception = ex
        }
        return noteList
    }
}