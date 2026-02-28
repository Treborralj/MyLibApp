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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mylib.R
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mylib.ui.components.StarRating
import androidx.compose.runtime.setValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Button
import com.example.mylib.ui.components.PostFrame


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
        viewModel.fetchReviews(bookId)
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
            var expanded by remember { mutableStateOf(false) }


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
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { expanded = true }
                    ) {
                        Text("Add to List")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Want to Read") },
                            onClick = {
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Currently Reading") },
                            onClick = {
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Read") },
                            onClick = {
                                expanded = false
                            }
                        )
                    }
                }
                //TEMPORARY STAR RATING, WILL BE MOVED TO REVIEWS?
                StarRating(
                    rating = userRating,
                    onRatingChange = { userRating = it }
                )



                when {
                    uiState.loadingReviews -> {
                        Text("Loading reviews...")
                    }
                    !uiState.reviews.isEmpty() -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            items(
                                uiState.reviews,
                            ){ item ->

                                PostFrame("Sample User", book.name, content = item)

                            }
                        }
                    }
                    uiState.reviews.isEmpty() -> {
                        Text("This book has no reviews yet")
                    }
                }






                Spacer(modifier = Modifier.weight(0.8f))

            }
        }
        else -> {
            Text("No book found")
        }
    }
}