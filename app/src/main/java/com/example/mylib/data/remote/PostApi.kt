package com.example.mylib.data.remote

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface PostApi {
    @POST("posts/add")
    suspend fun createPost(@Body() text: String? = null): PostResponse

    @PUT("posts/edit")
    suspend fun editPost(@Body() text: String? = null,
                         @Body() id: Int
                        ): PostResponse

    @DELETE("posts/remove/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}