package com.example.mylib.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity
import com.example.mylib.MainActivity.Companion.loggedInUser
import com.example.mylib.data.models.FollowRequest
import com.example.mylib.data.models.FollowResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ProfileResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import kotlin.collections.emptyList

data class ProfileUiState(
    val id: Int = -1,
    val username: String = "",
    val bio: String = "",
    val profilePic: String? = null,

    val posts: List<PostReviewItem.PostItem> = emptyList(),
    val reviews: List<PostReviewItem.ReviewItem> = emptyList(),

    val followers: List<FollowResponse> = emptyList(),
    val following: List<FollowResponse> = emptyList(),

    val loadingHeader: Boolean = false,
    val loadingBody: Boolean = false,

    val headerError: String = "",
    val bodyError: String = "",

    val viewingReviews: Boolean = false,
    val viewingFollowers: Boolean = false,
    val viewingFollowing: Boolean = false,
    val amFollowing: Boolean = false,

    )


class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val reviewRepository: ReviewRepository,

) : ViewModel(){

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()


    fun setViewingFollowers(b: Boolean) {
        _uiState.value = _uiState.value.copy(
            viewingFollowers = b,
        )
        if (b) {
            _uiState.value = _uiState.value.copy(
                viewingFollowing = false,
            )
        }
    }
    fun setViewingFollowing(b: Boolean) {
        _uiState.value = _uiState.value.copy(
            viewingFollowing = b,
        )
        if (b) {
            _uiState.value = _uiState.value.copy(
                viewingFollowers = false,
            )
        }
    }

    fun follow(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(headerError = "", loadingHeader = true)

                userRepository.followAccount(loggedInUser,username)

                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    amFollowing = true,
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    headerError = "Failed to follow account"
                )
            }
        }
    }

    fun unfollow(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(headerError = "", loadingHeader = true)

                userRepository.unfollowAccount(loggedInUser,username)
                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    amFollowing = false,
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    headerError = "Failed to unfollow account"
                )
            }
        }
    }

    fun loadFollowers(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(headerError = "", loadingHeader = true)
                val followers = userRepository.getFollowers(username);
                _uiState.value = _uiState.value.copy(
                    followers = followers,
                    loadingHeader = false,
                )
            } catch (e: Exception) {
                println("error loading followers: $e")
                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    headerError = "Failed to fetch followers"
                )
            }
        }
    }


    fun fetchProfile(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(headerError = "", bodyError = "", loadingHeader = true, loadingBody = true)

                val data = userRepository.getUserProfile(username);

                val bio = data.bio;

                when (bio) {
                    is String -> {
                        _uiState.value = _uiState.value.copy(
                            bio = bio
                        )
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(
                            bio = "No Bio"
                        )
                    }
                }

                run {
                    data.followers.forEach { follower ->
                        if (follower.username == loggedInUser) {
                            _uiState.value = _uiState.value.copy(
                                amFollowing = true,
                            )
                            return@run
                        }
                    }
                    _uiState.value = _uiState.value.copy(
                        amFollowing = false,
                    )
                }

                if (!data.posts.isEmpty()) {
                    val postsConverted = data.posts.map { PostReviewItem.PostItem(it) };

                    _uiState.value = _uiState.value.copy(
                        posts = postsConverted
                    )
                }

                if (!data.reviews.isEmpty()) {
                    val reviewsConverted = data.reviews.map { PostReviewItem.ReviewItem(it) };

                    _uiState.value = _uiState.value.copy(
                        reviews = reviewsConverted,
                    )
                }

                _uiState.value = _uiState.value.copy(
                    followers = data.followers,
                    following = data.following,
                    username = data.username,
                    profilePic = data.profilePictureBase64,
                    id = data.id,
                )

                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    loadingBody = false,
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    loadingBody = false,
                    headerError = "Failed to load profile"
                )
            }
        }
    }


    fun fetchPosts(username: String, viewingReviews: Boolean = false) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(bodyError = "", loadingBody = true, viewingReviews = viewingReviews)

                val posts = postRepository.getAccountPosts(username);
                if (posts.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        loadingBody = false,
                    )
                    return@launch
                }
                val postsConverted = posts.map { PostReviewItem.PostItem(it) };

                _uiState.value = _uiState.value.copy(
                    loadingBody = false,
                    posts = postsConverted
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingBody = false,
                    bodyError = "Failed to load posts"
                )
            }
        }
    }

    fun fetchReviews(username: String, viewingReviews: Boolean = true) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loadingBody = true, bodyError = "", viewingReviews = viewingReviews)

                val reviews = reviewRepository.fetchUserReviews(username);
                if (reviews.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        loadingBody = false,
                    )
                    return@launch
                }

                val reviewsConverted = reviews.map { PostReviewItem.ReviewItem(it) };

                _uiState.value = _uiState.value.copy(
                    loadingBody = false,
                    reviews = reviewsConverted
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingBody = false,
                    bodyError = "Failed to load reviews"
                )
            }
        }
    }
    fun updateProfilePicture(file: File) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    loadingHeader = true,
                    headerError = ""
                )

                userRepository.updateProfilePicture(file)
                fetchProfile(loggedInUser)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingHeader = false,
                    headerError = e.message ?: "Failed to update profile picture"
                )
            }
        }
    }
}