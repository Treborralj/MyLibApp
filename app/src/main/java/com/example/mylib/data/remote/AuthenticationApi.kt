package com.example.mylib.data.remote

import com.example.mylib.data.models.LoginRequest
import com.example.mylib.data.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST("auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse
}