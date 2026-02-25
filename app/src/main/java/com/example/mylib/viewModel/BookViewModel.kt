package com.example.mylib.viewModel

import BookRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.models.BookResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookUiState(
    val book: BookResponse? = null,
    //val reviews: List<Review> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class BookViewModel(
    private val repository: BookRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(BookUiState())

    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    fun fetchBook(bookId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loading = true, error = null)

                val book = repository.getBookById(bookId)
                if (book == null) {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = "Book not found"
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    book = book
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Failed to load book"
                )
            }
        }
    }
}

