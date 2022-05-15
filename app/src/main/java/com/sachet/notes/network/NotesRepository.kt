package com.sachet.notes.network

import android.util.JsonToken
import android.util.Log
import com.sachet.notes.data.Note
import com.sachet.notes.data.Response
import com.sachet.notes.model.DeleteNoteResponse
import com.sachet.notes.model.GetNoteResponse
import java.lang.Exception
import java.util.concurrent.CancellationException
import javax.inject.Inject

class NotesRepository
@Inject constructor(
    private val notesApi: NotesApi
){

    suspend fun saveNotes(token: String?, note: Note): Note{
        val noteSaved = Response<Note, Boolean, Exception>(null, true, null)
        return notesApi.saveNote(token, note)
    }

    suspend fun getAllNotes(
        token: String?,
    ): GetNoteResponse{
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
        note = notesApi.getNoteById(token, noteId)
        return note
    }

    suspend fun deleteNote(token: String?, noteId: String?):DeleteNoteResponse{
        return notesApi.deleteNoteById(token, noteId)
    }


}