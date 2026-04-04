    package com.example.mylib.data.models

data class PostResponse (
    val id: Int,
    val username: String,
    val title: String?,
    val text: String?,
    val time: String?,
    val imageBase64: String?,
    val imageType: String?

)