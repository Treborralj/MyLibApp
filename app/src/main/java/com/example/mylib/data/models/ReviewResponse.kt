package com.example.mylib.data.models

data class ReviewResponse(
    val id: Int,
    val text: String,
    val time: String,
    val score: Double,
    val accountId: Int,
    val username: String,
    val bookId: Int,
    val bookTitle: String
    )