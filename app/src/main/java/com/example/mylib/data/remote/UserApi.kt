package com.example.mylib.data.remote

import com.example.mylib.data.models.FollowResponse
import com.example.mylib.data.models.PostCreateRequest
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ProfileResponse
import com.example.mylib.data.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import kotlin.String

interface UserApi {
    @GET("account/discoverUser/{username}")
    suspend fun searchUser(@Path("username") username: String): List<UserResponse>

    @GET("account/feed")
    suspend fun fetchFeed(): List<PostResponse>

    @GET("account/profile/{username}")
    suspend fun getUserProfile(@Path("username") username: String): ProfileResponse


    @POST("account/followAccount")
    suspend fun followAccount(@Body() body: FollowResponse)


    @POST("account/unfollowAccount")
    suspend fun unfollowAccount(@Body() body: FollowResponse)

    @GET("account/getFollowing/{username}")
    suspend fun getFollowing(@Path("username") username: String): List<FollowResponse>

}