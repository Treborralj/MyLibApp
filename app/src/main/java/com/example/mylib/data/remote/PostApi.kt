package com.example.mylib.data.remote

import com.example.mylib.data.models.PostCreateRequest
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.PostUpdateRequest
import com.example.mylib.data.models.ReviewResponse
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.Part

interface PostApi {

    @GET("posts/account/{username}")
    suspend fun getAccountPosts(@Path("username") username: String): List<PostResponse>

    @Multipart
    @POST("posts/add")
    suspend fun createPost(
        @Part("title") title: RequestBody,
        @Part("text") text: RequestBody,
        @Part file: MultipartBody.Part?
    ): PostResponse

    @Multipart
    @PATCH("posts/edit")
    suspend fun editPost(
        @Part("id") id: RequestBody,
        @Part("title") title: RequestBody,
        @Part("text") text: RequestBody,
        @Part file: MultipartBody.Part?
    ): PostResponse

    @DELETE("posts/remove/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}