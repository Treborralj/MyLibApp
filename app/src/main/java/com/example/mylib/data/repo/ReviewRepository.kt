package com.example.mylib.data.repo

import com.example.mylib.data.models.ReviewCreateRequest
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.models.ReviewUpdateRequest
import com.example.mylib.data.remote.ReviewApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ReviewRepository(private val api: ReviewApi) {

    suspend fun fetchUserReviews(username: String): List<ReviewResponse>{
        return api.fetchUserReviews(username = username)
    }

    suspend fun fetchBookReviews(bookId: Int): List<ReviewResponse>{
        return api.fetchBookReviews(bookId = bookId)
    }

    suspend fun createReview(text: String, bookId: Int, score: Double): ReviewResponse {
        return api.createReview(ReviewCreateRequest(bookId,text,score))
    }

    suspend fun editReview(text: String, id: Int, score: Double): ReviewResponse {
        return api.editReview(ReviewUpdateRequest(id,text,score))
    }

    suspend fun deleteReview(id: Int) {
        return api.deleteReview(id)
    }
}

