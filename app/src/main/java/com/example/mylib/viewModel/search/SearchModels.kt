package com.example.mylib.viewModel.search

import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.UserResponse

sealed class SearchItem {
    data class BookItem(val book: BookResponse) : SearchItem()
    data class UserItem(val user: UserResponse) : SearchItem()
}
data class SearchUiState(
    val searchFor: SearchFor = SearchFor.BOOKS,
    val bookSearchBy: BookSearchBy = BookSearchBy.TITLE,
    val queryString: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val results: List<SearchItem> = emptyList(),
)
enum class SearchFor{
    BOOKS, USERS
}
enum class BookSearchBy {
    TITLE, AUTHOR, GENRE, ID
}