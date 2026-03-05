package com.example.mylib.data.remote

import com.example.mylib.data.models.BookRequest
import com.example.mylib.data.models.BookResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ListApi {
    @GET("account/getWantToRead")
    suspend fun getWantToRead(): List<BookResponse>
    @POST("account/wantToReadAdd")
    suspend fun addBookToWantToRead(@Body book: BookRequest)
    @DELETE("account/wantToReadRemove/{bookId}")
    suspend fun removeBookFromWantToRead(@Path("bookId") bookId: Int)

    @GET("account/amReading")
    suspend fun getAmReading(): List<BookResponse>
    @POST("account/amReadingAdd")
    suspend fun addBookToAmReading(@Body book: BookRequest)
    @DELETE("account/amReadingRemove/{bookId}")
    suspend fun removeBookFromAmReading(@Path("bookId") bookId: Int)

    @GET("account/haveRead")
    suspend fun getHaveRead(): List<BookResponse>
    @POST("account/haveReadAdd")
    suspend fun addBookToHaveRead(@Body book: BookRequest)
    @DELETE("account/haveReadRemove/{bookId}")
    suspend fun removeBookFromHaveRead(@Path("bookId") bookId: Int)
}