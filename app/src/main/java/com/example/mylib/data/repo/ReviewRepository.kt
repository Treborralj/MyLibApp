package com.example.mylib.data.repo

import com.example.mylib.data.models.ReviewRequest
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.ReviewApi
import com.example.mylib.data.repo.Dao.ReviewDao
import kotlinx.coroutines.flow.Flow


class ReviewRepository(
    private val api: ReviewApi,
    private val reviewDao: ReviewDao
) {

    fun observeBookReviews(bookId: Int): Flow<List<Review>> {
        return reviewDao.observeReviewsForBook(bookId)
    }

    suspend fun refreshBookReviews(bookId: Int) {
        val remoteReviews = api.fetchBookReviews(bookId)

        reviewDao.insertReviews(
            remoteReviews.map {
                Review(
                    id = it.id,
                    bookId = it.bookId,
                    username = it.username ?: "placeholder",
                    text = it.text,
                    score = it.score,
                    time = it.time
                )
            }
        )
    }

    suspend fun fetchUserReviews(username: String): List<ReviewResponse> {
        return api.fetchUserReviews(username)
    }

    suspend fun createReview(text: String?, bookId: Int, score: Double): ReviewResponse {
        val response = api.createReview(
            hashMapOf(
                "text" to (text ?: ""),
                "bookId" to bookId,
                "score" to score
            )
        )

        reviewDao.insertReview(
            Review(
                id = response.id,
                bookId = response.bookId,
                username = response.username ?: "placeholder",
                text = response.text,
                score = response.score,
                time = response.time
            )
        )
        return response
    }

    suspend fun editReview(text: String, id: Int, score: Double): ReviewResponse {
        val response = api.editReview(ReviewRequest(id, text, score))
        reviewDao.insertReview(
            Review(
                id = response.id,
                bookId = response.bookId,
                username = response.username ?: "placeholder",
                text = response.text,
                score = response.score,
                time = response.time
            )
        )
        return response
    }

    suspend fun deleteReview(id: Int) {
        api.deleteReview(id)
        // You might also want to delete it from the local DAO
        // reviewDao.deleteReviewById(id)
    }
}
