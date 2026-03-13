package com.example.mylib.data.models

class ProfileResponse (
    val id: Int,
    val username: String,
    val bio: String,
    val profilePictureBase64: String,
    val posts: List<PostResponse>,
    val reviews: List<ReviewResponse>,
    val followers: List<FollowResponse>,
    val following: List<FollowResponse>
)