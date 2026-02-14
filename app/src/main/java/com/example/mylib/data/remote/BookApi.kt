package com.example.mylib.data.remote

import com.example.mylib.data.models.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("books/search")
    suspend fun searchBooks(@Query("id") id: Int? = null,
                            @Query("name") name: String? = null,
                            @Query("genre") genre: String? = null,
                            @Query("isbn") isbn: String? = null,
                            @Query("writer") writer: String? = null,
                            @Query("score") score: Double? = null,
                            ): List<BookResponse>
}