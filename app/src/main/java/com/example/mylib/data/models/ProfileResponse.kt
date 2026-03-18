package com.example.mylib.data.models

data class ProfileResponse (
    val id: Int,
    var username: String,
    var bio: String,
    val profilePictureBase64: String,
    var posts: List<PostResponse>,
    var reviews: List<ReviewResponse>,
    var followers: List<FollowResponse>,
    var following: List<FollowResponse>
)