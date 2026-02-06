package com.example.mylib.data.repo

import com.example.mylib.data.models.LoginRequest
import com.example.mylib.data.models.LoginResponse
import com.example.mylib.data.remote.AuthenticationApi

class AuthenticationRepository(
    private val api: AuthenticationApi
){
    suspend fun login(username: String, password: String): LoginResponse{
        return api.login(LoginRequest(username, password))
    }
}