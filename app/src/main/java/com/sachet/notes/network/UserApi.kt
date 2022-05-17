package com.sachet.notes.network

import com.sachet.notes.data.LoginRequest
import com.sachet.notes.data.User
import com.sachet.notes.data.SignUpResponse
import com.sachet.notes.data.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface UserApi {

    @POST("save")
    suspend fun saveUser(@Body user: User): SignUpResponse
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse
    @GET("/profile")
    suspend fun getUser(): User?

}