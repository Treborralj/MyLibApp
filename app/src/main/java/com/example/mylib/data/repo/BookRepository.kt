package com.example.mylib.data.repo

import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.remote.BookApi

class BookRepository(private val api: BookApi) {
    suspend fun searchBooksByName(queryString: String): List<BookResponse>{
        if(queryString.isBlank()){
            return emptyList();
        }
        return api.searchBooks(name = queryString)
    }
    suspend fun getBookById(bookId: Int): BookResponse? {
        return api.searchBooks(id = bookId).firstOrNull()
    }
}