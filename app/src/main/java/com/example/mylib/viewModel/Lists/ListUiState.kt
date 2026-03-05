package com.example.mylib.viewModel.Lists

import com.example.mylib.data.models.BookResponse

data class ListUiState (
    val isLoading: Boolean = false,
    val error: String? = null,
    val expanded: Set<ListType> = emptySet(),
    val wantToRead: List<BookResponse> = emptyList(),
    val amReading: List<BookResponse> = emptyList(),
    val haveRead: List<BookResponse> = emptyList()
)

enum class ListType(val title: String){
    WANT_TO_READ("Want to Read"),
    AM_READING("Am Reading"),
    HAVE_READ("Have Read")
}

