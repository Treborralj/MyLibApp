package com.example.mylib.data.repo

import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.UpdateAccountRequest
import com.example.mylib.data.models.UpdateAccountResponse
import com.example.mylib.data.models.UpdatePasswordRequest
import com.example.mylib.data.remote.UserApi
import com.example.mylib.data.repo.Dao.PostDao

class UserRepository(
    private val api: UserApi,
    private val postDao: PostDao
) {

    suspend fun fetchFeed(): List<PostResponse> {
        return api.fetchFeed()
    }

    suspend fun updateAccount(username: String?, bio: String?): UpdateAccountResponse {
        return api.updateAccount(
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
        return api.updatePassword(
            UpdatePasswordRequest(
                oldPassword = oldPassword,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            )
        )
    }
}