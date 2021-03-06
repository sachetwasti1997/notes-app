package com.sachet.notes.network

import com.sachet.notes.data.Note
import com.sachet.notes.data.DeleteNoteResponse
import com.sachet.notes.data.GetNoteResponse
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface NotesApi {
    @POST("save")
    suspend fun saveNote(@Header("Authorization") token: String?, @Body note: Note): Note
    @GET("user")
    suspend fun getNotesOfUser(@Header("Authorization")token: String?): GetNoteResponse
    @GET("single/{noteId}")
    suspend fun getNoteById(@Header("Authorization") token: String?, @Path("noteId") noteId: String): Note?
    @DELETE("{noteId}")
    suspend fun deleteNoteById(@Header("Authorization") token: String?, @Path("noteId")noteId: String?): DeleteNoteResponse
}