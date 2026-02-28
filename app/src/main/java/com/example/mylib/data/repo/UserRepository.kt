package com.example.mylib.data.repo

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.ReviewApi
import com.example.mylib.data.remote.UserApi

class UserRepository(private val api: UserApi) {
    suspend fun fetchFeed(): List<PostResponse>{
        return api.fetchFeed()
    }
}