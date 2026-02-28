package com.example.mylib.data.repo

import com.example.mylib.data.models.BookResponse
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
}