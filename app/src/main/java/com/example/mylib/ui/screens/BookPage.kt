package com.example.mylib.ui.screens

import com.example.mylib.data.models.BookResponse
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mylib.viewModel.BookViewModel
import androidx.compose.runtime.getValue

@Composable
fun BookPage(
    bookId: Int,
    viewModel: BookViewModel,
    onAddReview: () -> Unit
) {
    // Observe state
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(bookId) {
        viewModel.fetchBook(bookId)
    }
    when {
        uiState.loading -> {
            Text("Loading...")
        }

        uiState.error != null -> {
            Text("Error: ${uiState.error}")
        }

        uiState.book != null -> {
            val book = uiState.book!!

            Column {
                Text(text = "ID: ${book.id}")
                Text(text = "Title: ${book.name}")
                Text(text = "Writer: ${book.writer}")
                Text(text = "Genre: ${book.genre}")
                Text(text = "ISBN: ${book.isbn}")
                Text(text = "Score: ${book.score}")
            }
        }

        else -> {
            Text("No book found")
        }
    }
}