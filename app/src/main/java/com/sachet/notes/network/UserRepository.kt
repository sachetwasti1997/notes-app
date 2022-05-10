package com.sachet.notes.network

import com.sachet.notes.data.LoginRequest
import com.sachet.notes.data.User
import com.sachet.notes.util.LoginResponse
import javax.inject.Inject

class UserRepository
@Inject constructor(
    private val userApi: UserApi
){
    suspend fun saveUser(user: User): User{
        return userApi.saveUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest): LoginResponse {
        return userApi.loginUser(loginRequest)
    }
}