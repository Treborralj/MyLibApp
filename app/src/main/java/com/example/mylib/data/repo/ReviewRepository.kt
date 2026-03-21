package com.example.mylib.data.repo

import com.example.mylib.data.models.ReviewRequest
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.ReviewApi
import com.example.mylib.data.repo.Dao.BookDao
import com.example.mylib.data.repo.Dao.ReviewDao
import com.example.mylib.data.repo.Dao.UserDao
import kotlinx.coroutines.flow.Flow

class ReviewRepository(
    private val api: ReviewApi,
    private val reviewDao: ReviewDao,
    private val bookDao: BookDao,
    private val userDao: UserDao
) {

    fun observeBookReviews(bookId: Int): Flow<List<Review>> {
        return reviewDao.observeReviewsForBook(bookId)
    }

    suspend fun refreshBookReviews(bookId: Int) {
        println("REFRESH START for bookId=$bookId")

        val remoteReviews = api.fetchBookReviews(bookId)

        println("REMOTE reviews count = ${remoteReviews.size}")
        remoteReviews.forEach {
            println("REMOTE review -> id=${it.id}, accountId=${it.accountId}, username=${it.username}, text=${it.text}, score=${it.score}")
        }

        val usersToInsert = remoteReviews
            .mapNotNull {
                val username = it.username
                if (username.isNullOrBlank()) return@mapNotNull null

                User(
                    id = it.accountId,
                    name = username,
                    bio = ""
                )
            }
            .distinctBy { it.id }

        if (usersToInsert.isNotEmpty()) {
            println("INSERTING ${usersToInsert.size} users into DB")
            userDao.insertUsers(usersToInsert)
        }

        val mapped = remoteReviews.mapNotNull {
            val username = it.username
            if (username.isNullOrBlank()) return@mapNotNull null

            Review(
                id = it.id,
                bookId = it.bookId,
                accountId = it.accountId,
                username = username,
                text = it.text,
                score = it.score,
                time = it.time
            )
        }

        println("INSERTING ${mapped.size} reviews into DB")
        reviewDao.deleteReviewsForBook(bookId)
        reviewDao.insertReviews(mapped)

        println("REFRESH DONE")
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
            hashMapOf(
                "text" to (text ?: ""),
                "bookId" to bookId,
                "score" to score
            )
        )

        println("CREATE response -> id=${response.id}, bookId=${response.bookId}, accountId=${response.accountId}, username=${response.username}, text=${response.text}, score=${response.score}")

        val username = response.username
        if (!username.isNullOrBlank()) {
            userDao.insert(
                User(
                    id = response.accountId,
                    name = username,
                    bio = ""
                )
            )

            reviewDao.insertReview(
                Review(
                    id = response.id,
                    bookId = response.bookId,
                    accountId = response.accountId,
                    username = username,
                    text = response.text,
                    score = response.score,
                    time = response.time
                )
            )
            println("INSERT DONE")
        } else {
            println("SKIPPED local insert because username was null/blank")
        }

        return response
    }

    suspend fun editReview(text: String, id: Int, score: Double): ReviewResponse {
        val response = api.editReview(ReviewRequest(id, text, score))
        val username = response.username

        if (!username.isNullOrBlank()) {
            userDao.insert(
                User(
                    id = response.accountId,
                    name = username,
                    bio = ""
                )
            )

            reviewDao.insertReview(
                Review(
                    id = response.id,
                    bookId = response.bookId,
                    accountId = response.accountId,
                    username = username,
                    text = response.text,
                    score = response.score,
                    time = response.time
                )
            )
        }

        return response
    }

    suspend fun deleteReview(id: Int) {
        api.deleteReview(id)
    }
}