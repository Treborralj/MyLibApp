package com.example.mylib.data.remote

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("account/discoverUser/{username}")
    suspend fun searchUser(@Path("username") username: String): List<UserResponse>

    @GET("account/feed")
    suspend fun fetchFeed(): List<PostResponse>
}