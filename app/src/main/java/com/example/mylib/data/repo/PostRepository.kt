package com.example.mylib.data.repo

import com.example.mylib.data.models.PostCreateRequest
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.PostUpdateRequest
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.PostApi
import com.example.mylib.data.remote.ReviewApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class PostRepository(private val api: PostApi) {

    suspend fun getAccountPosts(username: String): List<PostResponse> {
        return api.getAccountPosts(username)
    }
    suspend fun createPost(text: String): PostResponse {
        return api.createPost(PostCreateRequest(text = text))
    }

    suspend fun editPost(text: String, id: Int): PostResponse {
        return api.editPost(PostUpdateRequest(id = id, text = text))
    }

    suspend fun deletePost(id: Int) {
        return api.deletePost(id)
    }
}