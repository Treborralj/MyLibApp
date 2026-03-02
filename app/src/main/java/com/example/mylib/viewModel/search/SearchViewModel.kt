package com.example.mylib.viewModel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.data.repo.AppDatabase
import com.example.mylib.data.repo.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun loadDefault(){
        val state = _uiState.value
        if(state.searchFor == SearchFor.BOOKS && state.queryString.isBlank()){
            loadAllBooks()
        }
    }
    fun setSearchFor(newSearchFor: SearchFor){
        _uiState.value = _uiState.value.copy(
            searchFor = newSearchFor,
            queryString = "",
            error = null,
            results = emptyList()
        )
        if(newSearchFor == SearchFor.BOOKS){
            loadAllBooks()
        }
    }

    fun setBookSearchBy(newBookSearchBy: BookSearchBy){
        _uiState.value = _uiState.value.copy(
            bookSearchBy = newBookSearchBy,
            queryString = "",
            error = null,
            results = emptyList()
        )
        loadAllBooks()
    }

    fun onQueryStringChange(newQueryString: String){
        _uiState.value = _uiState.value.copy(
            queryString = newQueryString,
            error = null
        )
        val state = _uiState.value

        when(state.searchFor){
            SearchFor.BOOKS -> {
                if(newQueryString.isBlank()){
                    loadAllBooks()
                }else{
                    searchBooks(newQueryString)
                }
            }
            SearchFor.USERS -> {
                if(newQueryString.isBlank()){
                    _uiState.value = _uiState.value.copy(
                        results = emptyList(),
                        loading = false,
                        error = null
                    )
                }else{
                    searchUsers(newQueryString)
                }
            }
        }
    }
    private fun searchUsers(queryString: String){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null
            )
            try {
                val users = repository.findUserByUsername(queryString)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    results = users.map { SearchItem.UserItem(it) }
                )
            }catch (e : Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Search failed"
                )
            }
        }
    }

    private fun searchBooks(queryString: String){
        viewModelScope.launch{
            _uiState.value = _uiState.value.copy(
                loading = true,
                error = null
            )

            try{
                val state = _uiState.value

                val books = when(state.bookSearchBy){
                    BookSearchBy.TITLE -> {
                        repository.searchBooks(name = queryString)
                    }
                    BookSearchBy.AUTHOR -> {
                        repository.searchBooks(writer = queryString)
                    }

                    BookSearchBy.GENRE -> {
                        repository.searchBooks(genre = queryString)
                    }

                    BookSearchBy.ID -> {
                        val id = queryString.toIntOrNull()
                        if(id == null){
                            _uiState.value = _uiState.value.copy(
                                loading = false,
                                error = "ID must be a number",
                                results = emptyList()
                            )
                            return@launch
                        }
                        repository.searchBooks(id = id)
                    }
                }

                val sorted = books.sortedBy{ (it.name ?: "").lowercase()}

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    results = sorted.map{ SearchItem.BookItem(it)}
                )

            }catch(e: Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Search failed"
                )
            }
        }
    }
    fun loadAllBooks(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val books = repository.getAllBooks()
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    results = books.map{ SearchItem.BookItem(it)}
                )
            }catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Failed to load books"
                )
            }
        }
    }
}