package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.repo.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookSearchUiState(
    val queryString: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val results: List<BookResponse> = emptyList(),
)

class BookSearchViewModel(
    private val repository: BookRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(BookSearchUiState())
    val uiState = _uiState.asStateFlow()

    fun onQueryStringChange(newQueryString: String){
        _uiState.value = _uiState.value.copy(queryString = newQueryString)

        if(newQueryString.isBlank()){
            _uiState.value = _uiState.value.copy(results = emptyList())
        }
        searchBooks(newQueryString)
    }

    private fun searchBooks(queryString: String){
        viewModelScope.launch{
            try{

                _uiState.value = _uiState.value.copy(loading = true, error = null)
                val books = repository.searchBooksByName(queryString)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    results = books
                )

            }catch(e: Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Search failed"
                )
            }
        }
    }
}