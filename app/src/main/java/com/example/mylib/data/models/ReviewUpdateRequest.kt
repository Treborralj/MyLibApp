package com.example.mylib.data.models

data class ReviewUpdateRequest(
    val reviewId: Int?,
    val text: String?,
    val score: Double?
)