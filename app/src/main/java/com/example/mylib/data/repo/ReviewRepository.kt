package com.example.mylib.data.repo

import com.example.mylib.data.models.ReviewCreateRequest
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.models.ReviewUpdateRequest
import com.example.mylib.data.remote.ReviewApi
import com.example.mylib.data.repo.Dao.BookDao
import com.example.mylib.data.repo.Dao.ReviewDao
import kotlinx.coroutines.flow.Flow

class ReviewRepository(
    private val api: ReviewApi,
    private val reviewDao: ReviewDao,
    private val bookDao: BookDao
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
                    bookId = bookId,
                    bookName = it.bookTitle,
                    username = it.username ?: "placeholder",
                    text = it.text,
                    score = it.score,
                    time = it.time
                )
            }
        )
        updateLocalBookScore(bookId)
    }

    suspend fun updateLocalBookScore(bookId: Int) {
        val averageScore = reviewDao.getAverageScoreForBook(bookId)
        if (averageScore != null) {
            bookDao.updateBookScore(bookId, averageScore)
        }
    }

    suspend fun fetchUserReviews(username: String): List<ReviewResponse> {
        return api.fetchUserReviews(username)
    }

    suspend fun createReview(text: String?, bookId: Int, score: Double): ReviewResponse {
        val response = api.createReview(
            ReviewCreateRequest(
                text = text ?: "",
                bookId = bookId,
                score = score
            )
        )

        reviewDao.insertReview(
            Review(
                id = response.id,
                bookId = bookId,
                bookName = response.bookTitle,
                username = response.username ?: "placeholder",
                text = response.text,
                score = response.score,
                time = response.time
            )
        )
        updateLocalBookScore(bookId)
        return response
    }

    suspend fun editReview(text: String, id: Int, score: Double): ReviewResponse {
        val response = api.editReview(
            ReviewUpdateRequest(
                reviewId = id,
                text = text,
                score = score
            )
        )

        val updatedReview = Review(
            id = response.id,
            bookId = response.bookId,
            bookName = response.bookTitle,
            username = response.username ?: "placeholder",
            text = response.text,
            score = response.score,
            time = response.time
        )
        reviewDao.insertReview(updatedReview)
        updateLocalBookScore(updatedReview.bookId)
        return response
    }

    suspend fun deleteReview(id: Int) {
        val review = reviewDao.getReviewById(id)
        val bookId = review?.bookId
        
        api.deleteReview(id)
        reviewDao.deleteReview(id)
        
        if (bookId != null) {
            updateLocalBookScore(bookId)
        }
    }
}
