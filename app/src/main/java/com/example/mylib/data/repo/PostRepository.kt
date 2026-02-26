package com.example.mylib.data.repo

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.PostApi
import com.example.mylib.data.remote.ReviewApi

class PostRepository(private val api: PostApi) {
    suspend fun createPost(text: String): PostResponse {
        return api.createPost(text)
    }

    suspend fun editPost(text: String, id: Int): PostResponse {
        return api.editPost(text,id)
    }

    suspend fun deletePost(id: Int) {
        return api.deletePost(id)
    }
}