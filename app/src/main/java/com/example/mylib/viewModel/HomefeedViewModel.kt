package com.example.mylib.viewModel

import BookRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.models.UserResponse
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import com.example.mylib.viewModel.search.SearchItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomefeedUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val posts: List<PostReviewItem.PostItem> = emptyList()
)

sealed class PostReviewItem {
    data class PostItem(val post: PostResponse) : PostReviewItem()
    data class ReviewItem(val review: ReviewResponse) : PostReviewItem()
}


class HomefeedViewModel(
    private val repository: UserRepository,
) : ViewModel(){

    private val _uiState = MutableStateFlow(HomefeedUiState())

    val uiState: StateFlow<HomefeedUiState> = _uiState.asStateFlow()

    fun fetchFeed() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loading = true, error = null)

                val posts = repository.fetchFeed()
                if (posts.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    posts = posts.map { PostReviewItem.PostItem(it) }
                )
            } catch (e: Exception) {
                println("Failed to load posts")
                println("error: "+e.message)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                )
            }
        }
    }

}