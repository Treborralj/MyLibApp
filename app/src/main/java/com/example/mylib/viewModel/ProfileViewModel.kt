package com.example.mylib.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.models.PostResponse
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

    val posts: List<PostReviewItem.PostItem> = emptyList(),
    val postsError: String = "",
    val loadingPosts: Boolean = false,

    val reviews: List<PostReviewItem.ReviewItem> = emptyList(),
    val reviewsError: String = "",
    val loadingReviews: Boolean = false,
)


class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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