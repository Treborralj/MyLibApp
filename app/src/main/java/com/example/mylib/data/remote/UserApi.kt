package com.example.mylib.data.remote

import com.example.mylib.data.models.FollowRequest
import com.example.mylib.data.models.FollowResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ProfileResponse
import com.example.mylib.data.models.UpdateAccountRequest
import com.example.mylib.data.models.UpdateAccountResponse
import com.example.mylib.data.models.UpdatePasswordRequest
import com.example.mylib.data.models.UpdatePasswordResponse
import com.example.mylib.data.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("account/discoverUser/{username}")
    suspend fun searchUser(@Path("username") username: String): List<UserResponse>

    @GET("account/feed")
    suspend fun fetchFeed(): List<PostResponse>

    @GET("account/profile/{username}")
    suspend fun getUserProfile(@Path("username") username: String): ProfileResponse

    @POST("account/followAccount")
    suspend fun followAccount(@Body body: FollowRequest)

    @POST("account/unfollowAccount")
    suspend fun unfollowAccount(@Body body: FollowRequest)

    @GET("account/getFollowing/{username}")
    suspend fun getFollowing(@Path("username") username: String): List<FollowResponse>

    @GET("account/getFollowers/{username}")
    suspend fun getFollowers(@Path("username") username: String): List<FollowResponse>

    @PATCH("account/changeUsername")
    suspend fun updateAccount(@Body request: UpdateAccountRequest): UpdateAccountResponse

    @PATCH("account/changePassword")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): UpdatePasswordResponse
}