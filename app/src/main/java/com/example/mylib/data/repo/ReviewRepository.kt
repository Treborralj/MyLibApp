package com.example.mylib.data.repo

import com.example.mylib.data.models.ReviewRequest
import com.example.mylib.data.models.ReviewResponse
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

    suspend fun createReview(text: String?, bookId: Int, score: Double): ReviewResponse {
       var txt: String = ""
        if (text != null) {
            txt = text
        }
        val hashMap = HashMap<String, Any>();
        hashMap["text"] = '"'+txt+'"'
        hashMap["bookId"] = bookId
        hashMap["score"] = score
        return api.createReview(hashMap)
    }

    suspend fun editReview(text: String, id: Int, score: Double?): ReviewResponse {
        val body = ReviewRequest(id,text,score)
        return api.editReview(body)
    }

    suspend fun deleteReview(id: Int) {
        return api.deleteReview(id)
    }
}

