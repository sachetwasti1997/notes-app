package com.sachet.notes.network

import com.sachet.notes.data.Note
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface NotesApi {
    @POST("save")
    suspend fun saveNote(@Body note: Note): Note
    @GET("{userId}")
    suspend fun getNotesOfUser(@Path("userId") userId: String): ArrayList<Note>
    @GET("single/{noteId}")
    suspend fun getNoteById(@Path("noteId") noteId: String): Note?
}