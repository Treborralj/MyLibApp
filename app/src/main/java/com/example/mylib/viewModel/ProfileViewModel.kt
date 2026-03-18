package com.example.mylib.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity
import com.example.mylib.data.models.FollowResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ProfileResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class ProfileUiState(
    val viewingReviews: Boolean = false,
    val viewingFollowers: Boolean = false,
    val viewingFollowing: Boolean = false,

    val loading: Boolean = false,
    val error: String = "",
    val profileData: ProfileResponse? = null,

    val posts: List<PostReviewItem.PostItem> = emptyList(),
    val postsError: String = "",
    val loadingPosts: Boolean = false,

    val reviews: List<PostReviewItem.ReviewItem> = emptyList(),
    val reviewsError: String = "",
    val loadingReviews: Boolean = false,

    val amFollowing: Boolean = false,
    val followError: String = "",
    val loadingFollow: Boolean = false,
)


class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val reviewRepository: ReviewRepository
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
                _uiState.value = _uiState.value.copy(followError = "", loadingFollow = true)

                userRepository.followAccount(FollowResponse(username=username))

                _uiState.value = _uiState.value.copy(
                    loadingFollow = false,
                    amFollowing = true,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingFollow = false,
                    postsError = "Failed to follow account"
                )
            }
        }
    }

    fun unfollow(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(followError = "", loadingFollow = true)

                userRepository.unfollowAccount(FollowResponse(username=username))

                _uiState.value = _uiState.value.copy(
                    loadingFollow = false,
                    amFollowing = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingFollow = false,
                    postsError = "Failed to unfollow account"
                )
            }
        }
    }


    fun fetchProfile(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(error = "", loading = true)

                var data = userRepository.getUserProfile(username);


                println("profileData:\n"+
                        "id: "+data.id.toString()+"\n"+
                        "username: "+data.username+"\n"+
                        "bio: "+data.bio+"\n"+
                        "posts: "+data.posts+"\n"+
                        "reviews: "+data.reviews+"\n"+
                        "followers: "+data.followers+"\n"+
                        "following: "+data.following+"\n"
                )

                if(data.bio==null) {
                    data.bio = "No Bio"
                }

                _uiState.value = _uiState.value.copy(
                    profileData = data,
                )

                run {
                    data.followers.forEach { follower ->
                        if (follower.username == MainActivity.loggedInUser) {
                            _uiState.value = _uiState.value.copy(
                                amFollowing = true,
                            )
                            return@run
                        }
                    }
                }

                if (!data.posts.isEmpty()) {
                    val postsConverted = data.posts.map { PostReviewItem.PostItem(it) };

                    _uiState.value = _uiState.value.copy(
                        posts = postsConverted,
                    )
                }

                if (!data.reviews.isEmpty()) {
                    val reviewsConverted = data.reviews.map { PostReviewItem.ReviewItem(it) };

                    _uiState.value = _uiState.value.copy(
                        reviews = reviewsConverted,
                    )
                }

                _uiState.value = _uiState.value.copy(
                    loading = false,
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Failed to load profile"
                )
            }
        }
    }


    fun fetchPosts(username: String, viewingReviews: Boolean = false) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(postsError = "", loadingPosts = true, viewingReviews = viewingReviews)

                val posts = postRepository.getAccountPosts(username);
                if (posts.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        loadingPosts = false,
                    )
                    return@launch
                }
                val postsConverted = posts.map { PostReviewItem.PostItem(it) };

                _uiState.value = _uiState.value.copy(
                    loadingPosts = false,
                    posts = postsConverted,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingPosts = false,
                    postsError = "Failed to load posts"
                )
            }
        }
    }

    fun fetchReviews(username: String, viewingReviews: Boolean = true) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loadingReviews = true, reviewsError = "", viewingReviews = viewingReviews)

                val reviews = reviewRepository.fetchUserReviews(username);
                if (reviews.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        loadingReviews = false,
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    loadingReviews = false,
                    reviews = reviews.map { PostReviewItem.ReviewItem(it) },
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loadingReviews = false,
                    reviewsError = "Failed to load reviews"
                )
            }
        }
    }

}