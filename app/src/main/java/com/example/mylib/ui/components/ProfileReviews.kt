package com.example.mylib.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.MainActivity
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.PostReviewItem
import com.example.mylib.viewModel.ProfileViewModel

@Composable
fun ProfileReviews(
    viewModel: ProfileViewModel,
    navController: NavController,
    username: String,
) {
    val uiState by viewModel.uiState.collectAsState();

    val refreshReviews =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("refreshReviews", false)
            ?.collectAsState()

    LaunchedEffect(refreshReviews?.value) {
        if (refreshReviews?.value == true) {
            viewModel.fetchReviews(username)
            navController.currentBackStackEntry?.savedStateHandle?.set("refreshReviews", false)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        when {
            !uiState.reviewsError.isEmpty() -> {
                Text(
                    text = uiState.reviewsError,
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.loadingReviews -> {
                Text(
                    text = "Loading reviews...",
                )
            }
            uiState.reviews.isEmpty() && !uiState.loadingReviews -> {
                Text("This account has no reviews yet")
            }

            true -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items<PostReviewItem.ReviewItem>(
                        items = uiState.reviews
                    ) { item ->
                        if (username == MainActivity.loggedInUser) {
                            Column(){
                                PostFrame(
                                    username,
                                    "Book Title",
                                    content = PostReviewItem.ReviewItem(item.review)
                                )

                                Button(onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("reviewId", item.review.id)
                                    navController.currentBackStackEntry?.savedStateHandle?.set("reviewText", item.review.text)
                                    navController.currentBackStackEntry?.savedStateHandle?.set("reviewTime", item.review.time)
                                    navController.currentBackStackEntry?.savedStateHandle?.set("reviewScore", item.review.score)
                                    navController.currentBackStackEntry?.savedStateHandle?.set("reviewBookId", item.review.bookId)
                                    navController.navigate(Routes.ReviewEditor.route)
                                }) {
                                    Text("Edit")
                                }
                            }
                        }
                        else {
                            PostFrame(
                                username,
                                "Book Title",
                                content = PostReviewItem.ReviewItem(item.review)
                            )
                        }
                    }
                }
            }
        }
    }

}