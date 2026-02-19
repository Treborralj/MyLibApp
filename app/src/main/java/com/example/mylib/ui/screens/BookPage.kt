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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mylib.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mylib.ui.components.StarRating
import androidx.compose.runtime.setValue

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
            var userRating by remember(bookId) { mutableStateOf(0f) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(0.2f))

                Image(
                    painter = painterResource(R.drawable.book_cover_placeholder),
                    contentDescription = "Book cover",
                    modifier = Modifier.size(width = 160.dp, height = 220.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = book.name ?: "Unknown Title",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Written by ${book.writer}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "ISBN ${book.isbn}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))


                Text(
                    text = "Genre: ${book.genre}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Score: ${book.score}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your rating: $userRating / 5",
                    style = MaterialTheme.typography.bodyMedium
                )

                StarRating(
                    rating = userRating,
                    onRatingChange = { userRating = it }
                )
                Spacer(modifier = Modifier.weight(0.8f))

            }
        }
        else -> {
            Text("No book found")
        }
    }
}