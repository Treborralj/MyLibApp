package com.example.mylib.data.remote

import com.example.mylib.data.models.PostRequest
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface PostApi {

    @GET("posts/account/{username}")
    suspend fun getAccountPosts(@Path("username") username: String): List<PostResponse>

    @POST("posts/add")
    suspend fun createPost(@Body() body: PostRequest): PostResponse

    @PATCH("posts/edit")
    suspend fun editPost(@Body() body:PostRequest): PostResponse

    @DELETE("posts/remove/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}