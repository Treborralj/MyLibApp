package com.example.mylib.data.remote

import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.LoginRequest
import com.example.mylib.data.models.LoginResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Path

interface ReviewApi {
    @GET("reviews/account/{username}")
    suspend fun fetchUserReviews(@Path("username") username: String,
    ): List<ReviewResponse>

    @GET("reviews/book/{bookId}")
    suspend fun fetchBookReviews(@Path("bookId") bookId: Int,
    ): List<ReviewResponse>

    @POST("reviews/add")
    suspend fun createReview(@Body() text: String? = null,
                           @Body() bookId: Int,
                           @Body() score: Double? = null): ReviewResponse

    @PUT("reviews/edit")
    suspend fun editReview(@Body() text: String? = null,
                         @Body() reviewId: Int,
                         @Body() score: Double? = null
    ): ReviewResponse

    @DELETE("reviews/remove/{id}")
    suspend fun deleteReview(@Path("id") id: Int)
}