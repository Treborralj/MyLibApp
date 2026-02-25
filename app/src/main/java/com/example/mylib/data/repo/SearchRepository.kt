package com.example.mylib.data.repo

import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.UserResponse
import com.example.mylib.data.remote.BookApi
import com.example.mylib.data.remote.UserApi

class SearchRepository(private val bookapi: BookApi, userApi: UserApi) {
    suspend fun getAllBooks(): List<BookResponse>{
        return bookapi.getAllBooks()
    }

    suspend fun searchBooks(
        id: Int? = null,
        name: String? = null,
        genre: String? = null,
        writer: String? = null
    ): List<BookResponse>{
        return bookapi.searchBooks(
            id = id,
            name = name,
            genre = genre,
            writer = writer
        )
    }
    suspend fun findUserByUsername(username: String): List<UserResponse>{
        if(username.isBlank()){
            return emptyList()
        }
        return bookapi.findUser(username)
    }
}