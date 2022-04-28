package com.sachet.notes.network

import com.sachet.notes.data.Note
import retrofit2.http.*
import java.util.*
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
interface NotesApi {
    @POST("save")
    suspend fun saveNote(@Body note: Note): Note
    @GET("{userId}")
    suspend fun getNotesOfUser(@Path("userId") userId: String): ArrayList<Note>
    @GET("single/{noteId}")
    suspend fun getNoteById(@Path("noteId") noteId: String): Note?
    @DELETE("{noteId}")
    suspend fun deleteNoteById(@Path("noteId")noteId: String): String
}