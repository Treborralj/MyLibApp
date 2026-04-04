package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity.Companion.loggedInUser
import com.example.mylib.data.models.BookRequest
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.repo.Book
import com.example.mylib.data.repo.BookRepository
import com.example.mylib.data.repo.Review
import com.example.mylib.data.repo.ListRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.viewModel.Lists.ListType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookUiState(
    val book: BookResponse? = null,
    //val reviews: List<Review> = emptyList(),
    val loading: Boolean = false,
    val loadingReviews: Boolean = false,
    val error: String? = null,
    val reviews: List<PostReviewItem.ReviewItem> = emptyList(),
)


class BookViewModel(
    private val repository: BookRepository,
    private val reviewRepository: ReviewRepository,
    private val listRepository: ListRepository,
) : ViewModel(){

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    fun loadBook(bookId: Int) {
        viewModelScope.launch {
            repository.observeBook(bookId)
                .collect { book ->
                    _uiState.update {
                        it.copy(
                            book = book?.toBookResponse(), // map entity → UI model
                            loading = false
                        )
                    }
                }
            repository.updateLocalBookScore(bookId)
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            repository.refreshBook(bookId)
        }
    }


    fun loadReviews(bookId: Int) {
        viewModelScope.launch {
            reviewRepository.observeBookReviews(bookId)
                .collect { reviews ->
                    _uiState.update {
                        it.copy(
                            reviews = reviews.map {
                                PostReviewItem.ReviewItem(it.toReviewResponse())
                            },
                            loadingReviews = false
                        )
                    }
                }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loadingReviews = true) }
            reviewRepository.refreshBookReviews(bookId)
        }
    }
    fun createReview(bookId: Int, score: Float, text: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    error = null,
                    loadingReviews = true
                )

                reviewRepository.createReview(
                    text = text,
                    bookId = bookId,
                    score = score.toDouble()
                )

                reviewRepository.refreshBookReviews(bookId)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to create review"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    loadingReviews = false
                )
            }
        }
    }

    fun addBookToList(listType: ListType, bookId: Int){
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(error = null)

                val request = BookRequest(id = bookId)

                when(listType){
                    ListType.WANT_TO_READ -> listRepository.addBookToWantToRead(loggedInUser,request)
                    ListType.AM_READING -> listRepository.addBookToAmReading(loggedInUser,request)
                    ListType.HAVE_READ -> listRepository.addBookToHaveRead(loggedInUser,request)
                }

            } catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }

    }
}

fun Review.toReviewResponse(): ReviewResponse {
    return ReviewResponse(
        id = id,
        bookId = bookId,
        text = text ?: "",
        score = score,
        time = time ?: "",
        username = username
    )
}
fun Book.toBookResponse(): BookResponse {
    return BookResponse(
        id = id,
        name = name,
        genre = genre,
        isbn = isbn,
        writer = writer,
        score = score
    )
}
