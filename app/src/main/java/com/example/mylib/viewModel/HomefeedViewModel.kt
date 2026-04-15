package com.example.mylib.viewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.models.UserResponse
import com.example.mylib.data.repo.ImageStorageManager
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
    val posts: List<PostReviewItem.PostItem> = emptyList(),
    val currentUser: String = ""
)

sealed class PostReviewItem {
    data class PostItem(val post: PostResponse) : PostReviewItem()
    data class ReviewItem(val review: ReviewResponse) : PostReviewItem()
}


class HomefeedViewModel(
    private val repository: UserRepository,
    private val storageManager: ImageStorageManager
) : ViewModel(){

    private val _uiState = MutableStateFlow(HomefeedUiState())

    val uiState: StateFlow<HomefeedUiState> = _uiState.asStateFlow()

    val profilePictures = mutableStateMapOf<String, String>() // username -> localPath

    fun resolveProfilePicture(username: String) {
        if (profilePictures.containsKey(username)) return

        viewModelScope.launch {
            val localPath = storageManager.getImagePathForUser(username)
            if (localPath != null) {
                profilePictures[username] = localPath
            } else {
                try {
                    val savedPath = repository.getProfilePicture(username)
                    if (savedPath != null) {
                        profilePictures[username] = savedPath
                    }
                } catch (e: Exception) {
                    // Fallback handled by UI
                }
            }
        }
    }

    fun fetchFeed() {
        val loggedInUser = MainActivity.loggedInUser
        
        // If the user has changed, clear the existing feed
        if (_uiState.value.currentUser != loggedInUser) {
            _uiState.value = HomefeedUiState(currentUser = loggedInUser)
            profilePictures.clear()
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loading = true, error = null)

                val posts = repository.fetchFeed()
                
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    posts = posts.map { PostReviewItem.PostItem(it) }
                )
            } catch (e: Exception) {
                println("Failed to load posts")
                println("error: "+e.message)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Failed to load feed"
                )
            }
        }
    }

}