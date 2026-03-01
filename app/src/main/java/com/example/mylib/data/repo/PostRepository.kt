package com.example.mylib.data.repo

import com.example.mylib.data.models.PostResponse
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
        val hashMap = HashMap<String, Any>();
        hashMap.put("text",text)
        return api.createPost(hashMap)
    }

    suspend fun editPost(text: String, id: Int): PostResponse {
        val hashMap = HashMap<String, Any>();
        hashMap.put("text",text)
        hashMap.put("id",id)
        return api.editPost(hashMap)
    }

    suspend fun deletePost(id: Int) {
        return api.deletePost(id)
    }
}