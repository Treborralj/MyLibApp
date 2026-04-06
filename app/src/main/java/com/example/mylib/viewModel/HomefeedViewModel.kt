package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.models.UserResponse
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import com.example.mylib.viewModel.search.SearchItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.map

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

    var postFlow: Flow<List<PostReviewItem.PostItem>> = flow { emit(List(1,{PostReviewItem.PostItem(
        PostResponse(
            id=-1,
            username="",
            title ="",
            text ="",
            time ="",
            imageType = "png",
            imageBase64 = null,
            profilePic = null,
        ))}))}

    var uiPostState: StateFlow<List<PostReviewItem.PostItem>> = postFlow.stateIn(
        scope = viewModelScope,                      // where it lives
        started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
        initialValue = List(1,{ PostReviewItem.PostItem(PostResponse(
            id = -1,
            username = "",
            title="",text="",
            time="",
            imageBase64 = "",
            imageType = "",
            profilePic = null,
        ))}  )
    )

    fun fetchFeed() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loading = true, error = null)


                postFlow = repository.fetchFeed().map{ it.reversed() }
                uiPostState = postFlow.stateIn(
                    scope = viewModelScope,                      // where it lives
                    started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
                    initialValue = List(1,{ PostReviewItem.PostItem(PostResponse(
                        id = -1,
                        username = "",
                        title="",text="",
                        time="",
                        imageBase64 = "",
                        imageType = "",
                        profilePic = "",
                    ))}  )
                )

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    //posts = posts.map { PostReviewItem.PostItem(it) }
                   // posts = posts
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