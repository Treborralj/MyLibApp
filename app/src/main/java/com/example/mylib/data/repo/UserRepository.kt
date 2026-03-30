package com.example.mylib.data.repo

import com.example.mylib.data.models.DeleteAccountRequest
import com.example.mylib.data.models.FollowRequest
import com.example.mylib.data.models.FollowResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ProfilePictureResponse
import com.example.mylib.data.models.UpdateAccountRequest
import com.example.mylib.data.models.UpdateAccountResponse
import com.example.mylib.data.models.UpdatePasswordRequest
import com.example.mylib.data.models.ProfileResponse
import com.example.mylib.data.remote.UserApi
import com.example.mylib.data.repo.Dao.PostDao
import com.example.mylib.data.repo.Dao.UserDao
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepository(
    private val userApi: UserApi,
    private val postDao: PostDao,
    private val userDao: UserDao,
    private val imageStorage: ImageStorageManager
) {
    suspend fun fetchFeed(): List<PostResponse>{
        return userApi.fetchFeed()
    }

    suspend fun fetchAndStorePhoto(name:String){
        val response = userApi.getProfilePicture(name)

        val path = imageStorage.saveBase64Image(response.imageBase64, /*response.type*/"PNG" , name)
        userDao.updateImage(name,path)

    }

    suspend fun updateAccount(username: String?, bio: String?): UpdateAccountResponse {
        return userApi.updateAccount(
            UpdateAccountRequest(
                username = username,
                bio = bio
            )
        )
    }

    suspend fun updatePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): String {
        return userApi.updatePassword(
            UpdatePasswordRequest(
                oldPassword = oldPassword,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            )
        ).message
    }

    suspend fun getUserProfile(username: String): ProfileResponse {
        return userApi.getUserProfile(username)
    }

    suspend fun followAccount(account: FollowRequest) {
        userApi.followAccount(account)
    }

    suspend fun unfollowAccount(account: FollowRequest) {
        userApi.unfollowAccount(account)
    }

    suspend fun getFollowing(username: String): List<FollowResponse> {
        return userApi.getFollowing(username)
    }

    suspend fun getFollowers(username: String): List<FollowResponse> {
        return userApi.getFollowers(username)
    }
    suspend fun getProfilePicture(username: String): ProfilePictureResponse {
        return userApi.getProfilePicture(username)
    }

    suspend fun updateProfilePicture(file: File): ProfilePictureResponse {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestBody
        )
        return userApi.updateProfilePicture(filePart)
    }

    suspend fun deleteAccount(password: String) {
        userApi.deleteAccount(DeleteAccountRequest(password))
    }
}