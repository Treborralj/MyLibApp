package com.example.mylib.data.repo

import com.example.mylib.data.models.BookRequest
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.remote.ListApi

class ListRepository(private val api: ListApi) {
    suspend fun getWantToRead(): List<BookResponse>{
        return api.getWantToRead()
    }
    suspend fun addBookToWantToRead(book: BookRequest){
        return api.addBookToWantToRead(book)
    }
    suspend fun removeBookFromWantToRead(bookId: Int){
        api.removeBookFromWantToRead(bookId)
    }

    suspend fun getAmReading(): List<BookResponse>{
        return api.getAmReading()
    }
    suspend fun addBookToAmReading(book: BookRequest){
        return api.addBookToAmReading(book)
    }
    suspend fun removeBookFromAmReading(bookId: Int){
        api.removeBookFromAmReading(bookId)
    }

    suspend fun getHaveRead(): List<BookResponse>{
        return api.getHaveRead()
    }
    suspend fun addBookToHaveRead(book: BookRequest){
        return api.addBookToHaveRead(book)
    }
    suspend fun removeBookFromHaveRead(bookId: Int){
        api.removeBookFromHaveRead(bookId)
    }
}