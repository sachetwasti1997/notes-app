package com.sachet.notes.network

import com.sachet.notes.data.LoginRequest
import com.sachet.notes.data.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface UserApi {

    @POST
    suspend fun saveUser(@Body user: User): User
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): String
    @GET("/profile")
    suspend fun getUser(): User?

}