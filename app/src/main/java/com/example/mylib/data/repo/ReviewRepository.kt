package com.example.mylib.data.repo

import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.BookApi
import com.example.mylib.data.remote.ReviewApi

class ReviewRepository(private val api: ReviewApi) {
    suspend fun fetchUserReviews(username: String): List<ReviewResponse>{
        return api.fetchUserReviews(username = username)
    }

    suspend fun fetchBookReviews(bookId: Int): List<ReviewResponse>{
        return api.fetchBookReviews(bookId = bookId)
    }

    suspend fun createReview(text: String?, bookId: Int, score: Double?): ReviewResponse {
        return api.createReview(text,bookId,score)
    }

    suspend fun editReview(text: String, id: Int): ReviewResponse {
        return api.editReview(text,id)
    }

    suspend fun deleteReview(id: Int) {
        return api.deleteReview(id)
    }
}