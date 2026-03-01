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
    val loadingPosts: Boolean = false,
    val loadingReviews: Boolean = false,
    val error: String? = null,
    val posts: List<PostReviewItem.PostItem> = emptyList(),
    val reviews: List<PostReviewItem.ReviewItem> = emptyList(),
    val viewingReviews: Boolean = false,
)


class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun fetchPosts(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loadingPosts = true, error = null, viewingReviews = false)

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
                println("Failed to load posts")
                println("error: "+e.message)
                _uiState.value = _uiState.value.copy(
                    loadingPosts = false,
                    error = "Failed to load posts"
                )
            }
        }
    }

    fun fetchReviews(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loadingReviews = true, error = null, viewingReviews = true)

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
                println("Failed to load reviews")
                println("error: "+e.message)
                _uiState.value = _uiState.value.copy(
                    loadingReviews = false,
                    error = "Failed to load reviews"
                )
            }
        }
    }

}