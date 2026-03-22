package com.example.mylib.data.remote

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.UserResponse
import com.example.mylib.data.models.UpdateAccountRequest
import com.example.mylib.data.models.UpdateAccountResponse
import com.example.mylib.data.models.UpdatePasswordRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PATCH
interface UserApi {
    @GET("account/discoverUser/{username}")
    suspend fun searchUser(@Path("username") username: String): List<UserResponse>

    @GET("account/feed")
    suspend fun fetchFeed(): List<PostResponse>

    @POST("account/followAccount")
    suspend fun follow(@Body body: HashMap<String, Any>)

    @POST("account/unfollowAccount")
    suspend fun unfollow(@Body body: HashMap<String, Any>)

    @GET("account/getFollowing/{username}")
    suspend fun getFollowing(@Path("username") username: String): List<String>

    @GET("account/getFollowers/{username}")
    suspend fun getFollowers(@Path("username") username: String): List<String>

    @PATCH("account/changeUsername")
    suspend fun updateAccount(@Body request: UpdateAccountRequest): UpdateAccountResponse

    @PATCH("account/changePassword")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): String

}