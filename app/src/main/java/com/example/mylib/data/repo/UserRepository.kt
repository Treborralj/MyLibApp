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
import com.example.mylib.data.repo.Dao.FollowingDao
import com.example.mylib.data.repo.Dao.PostDao
import com.example.mylib.data.repo.Dao.ReviewDao
import com.example.mylib.data.repo.Dao.UserDao
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepository(
    private val userApi: UserApi,
    private val followingDao: FollowingDao,
    private val postDao: PostDao,
    private val userDao: UserDao,
    private val reviewDao: ReviewDao,
    private val imageStorage: ImageStorageManager
) {
    suspend fun fetchFeed(): List<PostResponse>{
        val response = userApi.fetchFeed()

        postDao.insertAll(response.map {
            var path: String? = null
            if (it.imageBase64 != null && it.imageType != null) {
                path = imageStorage.saveBase64Image(it.imageBase64, it.imageType, "post_" + it.id)
            }
            Post(
                id = it.id,
                username = it.username,
                title = it.title,
                text = it.text,
                time = it.time,
                imagePath = path
            )
        })
        return response
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
        val response = userApi.getUserProfile(username)
        
        var profileType = /*response.imageType ?:*/ "jpg"
        val path = if (response.profilePictureBase64 != null) {
            imageStorage.saveBase64Image(response.profilePictureBase64, profileType, response.username)
        } else null

        userDao.insert(User(
            id = response.id,
            name = response.username,
            bio = response.bio,
            imagePath = path
        ))

        response.posts?.let { posts ->
            postDao.insertAll(posts.map {
                var postPath: String? = null
                if (it.imageBase64 != null && it.imageType != null) {
                    postPath = imageStorage.saveBase64Image(it.imageBase64, it.imageType, "post_" + it.id)
                }
                Post(
                    id = it.id,
                    username = it.username,
                    title = it.title,
                    text = it.text,
                    time = it.time,
                    imagePath = postPath
                )
            })
        }

        response.reviews?.let { reviews ->
            reviewDao.insertAll(reviews.map {
                Review(
                    id = it.id,
                    bookId = it.bookId,
                    username = it.username ?: response.username,
                    text = it.text,
                    score = it.score,
                    time = it.time
                )
            })
        }

        response.followers?.let { followers ->
            val followerEntities = followers.map { 
                Following(followingUsername = it.username, followedUsername = response.username) 
            }
            followingDao.clearFollowersAndInsert(response.username, followerEntities)
        }

        response.following?.let { following ->
            val followingEntities = following.map { 
                Following(followingUsername = response.username, followedUsername = it.username) 
            }
            followingDao.clearFollowingAndInsert(response.username, followingEntities)
        }

        // I cant be assed to recreate a response from local, and should be done on viewmodel level
        return response
    }

    suspend fun followAccount(loggedinUser: String, username: String) {
        userApi.followAccount(FollowRequest(username))

        followingDao.insert(
            Following(
                followingUsername = loggedinUser,
                followedUsername = username
            )
        )
    }

    suspend fun unfollowAccount(loggedinUser: String, username: String) {
        userApi.unfollowAccount(FollowRequest(username))

        followingDao.delete(
            Following(
                followingUsername = loggedinUser,
                followedUsername = username
            )
        )
    }

    suspend fun getFollowers(loggedinUser: String): List<FollowResponse> {
        val remoteFollowers = userApi.getFollowers(loggedinUser)

        val followerEntities = remoteFollowers.map { followResponse ->
            Following(
                followingUsername = followResponse.username,
                followedUsername = loggedinUser
            )
        }
        followingDao.clearFollowersAndInsert(loggedinUser, followerEntities)

        return remoteFollowers
    }

    suspend fun getFollowing(loggedinUser: String): List<FollowResponse> {
        val remoteFollowing = userApi.getFollowing(loggedinUser)

        val followingEntities = remoteFollowing.map { followResponse ->
            Following(
                followingUsername = loggedinUser,
                followedUsername = followResponse.username
            )
        }
        followingDao.clearFollowingAndInsert(loggedinUser, followingEntities)

        return remoteFollowing
    }

    suspend fun getProfilePicture(username: String): String? {
        val response = userApi.getProfilePicture(username)
        val type = response.type ?: "jpg"
        val path = imageStorage.saveBase64Image(response.imageBase64, type, username)
        userDao.updateImage(username, path)
        return userDao.getImagePath(username)
    }

    suspend fun updateProfilePicture(username: String, file: File): ProfilePictureResponse {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestBody
        )

        val response = userApi.updateProfilePicture(filePart)
        
        // After successful upload, refresh the local image path
        val path = imageStorage.saveBase64Image(response.imageBase64, response.type ?: "jpg", username)
        userDao.updateImage(username, path)

        return response
    }

    suspend fun deleteAccount(password: String) {
        userApi.deleteAccount(DeleteAccountRequest(password))
    }
}
