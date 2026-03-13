package com.example.mylib.data.repo

import com.example.mylib.data.models.FollowResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ProfileResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.ReviewApi
import com.example.mylib.data.remote.UserApi

class UserRepository(private val api: UserApi) {
    suspend fun fetchFeed(): List<PostResponse>{
        return api.fetchFeed()
    }

    suspend fun getUserProfile(username: String): ProfileResponse{
        return api.getUserProfile(username)
    }

    suspend fun followAccount(account: FollowResponse) {
        return api.followAccount(account)
    }

    suspend fun unfollowAccount(account: FollowResponse) {
        return api.unfollowAccount(account)
    }


    suspend fun getFollowing(username: String): List<FollowResponse>{
        return api.getFollowing(username)
    }
}