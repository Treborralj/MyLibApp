package com.example.mylib.data.models

data class ReviewRequest(
    val reviewId: Int,
    val text: String,
    val score: Double
)