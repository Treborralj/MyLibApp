package com.example.mylib.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.navigation.NavHost
import com.example.mylib.MainActivity
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.ui.components.PostFrame
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.BookViewModel
import com.example.mylib.viewModel.HomefeedViewModel
import com.example.mylib.viewModel.PostReviewItem
import com.example.mylib.viewModel.ProfileViewModel

@Composable
fun ProfilePage(
    username: String,
    navController: NavController,
    viewModel: ProfileViewModel,
){
    // Observe state
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = MainActivity.bearerToken) {
        viewModel.fetchPosts(username)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        when {
            !uiState.viewingReviews -> {
                Button(onClick = { viewModel.fetchReviews(username) }) {
                    Text("See Reviews")
                }
                Text(
                    text = "Posts",
                    style = MaterialTheme.typography.headlineMedium
                )

                uiState.error?.let{ msg ->
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ){
                    when {
                        uiState.loadingPosts -> {
                            Text("Loading posts...")
                        }
                        !uiState.posts.isEmpty() -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ){
                                items(
                                    uiState.posts,
                                ){ item ->
                                    if (username == MainActivity.loggedInUser) {
                                        Column(){
                                            PostFrame(username, "Post Title", content = PostReviewItem.PostItem(item.post));

                                            Button(onClick = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set("postId", item.post.id)
                                                navController.currentBackStackEntry?.savedStateHandle?.set("postText", item.post.text)
                                                navController.currentBackStackEntry?.savedStateHandle?.set("postTime", item.post.time);

                                                navController.navigate(Routes.PostEditor.route);
                                            }) {
                                                Text("Edit")
                                            }
                                        }
                                    }
                                    else {
                                        PostFrame(username, "Post Title", content = PostReviewItem.PostItem(item.post));
                                    }


                                }
                            }
                        }
                        uiState.posts.isEmpty() -> {
                            Text("This account has no posts yet")
                        }
                    }
                }
            }

            uiState.viewingReviews -> {
                Button(onClick = { viewModel.fetchPosts(username) }) {
                    Text("See Posts")
                }
                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.headlineMedium
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ){
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

                                    if (username == MainActivity.loggedInUser) {
                                        Column(){
                                            PostFrame(username, "Book Title", content = PostReviewItem.ReviewItem(item.review));

                                            Button(onClick = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set("reviewId", item.review.id)
                                                navController.currentBackStackEntry?.savedStateHandle?.set("reviewText", item.review.text)
                                                navController.currentBackStackEntry?.savedStateHandle?.set("reviewTime", item.review.time);
                                                navController.currentBackStackEntry?.savedStateHandle?.set("reviewScore", item.review.score)
                                                navController.navigate(Routes.ReviewEditor.route);
                                            }) {
                                                Text("Edit")
                                            }
                                        }
                                    }
                                    else {
                                        PostFrame(username, "Book Title", content = PostReviewItem.ReviewItem(item.review));
                                    }

                                }
                            }
                        }
                        uiState.reviews.isEmpty() -> {
                            Text("This account has no reviews yet")
                        }
                    }
                }
            }
        }


    }


}