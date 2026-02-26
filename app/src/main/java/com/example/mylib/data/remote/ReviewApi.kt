package com.example.mylib.data.remote

import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.LoginRequest
import com.example.mylib.data.models.LoginResponse
import com.example.mylib.data.models.ReviewResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

interface ReviewApi {
    @GET("reviews/account/{username}")
    suspend fun fetchUserReviews(@Path("username") username: String,
    ): List<ReviewResponse>

    @GET("reviews/book/{bookId}")
    suspend fun fetchBookReviews(@Path("bookId") bookId: Int,
    ): List<ReviewResponse>
}