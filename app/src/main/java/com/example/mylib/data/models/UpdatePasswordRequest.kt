package com.example.mylib.data.models

data class UpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)