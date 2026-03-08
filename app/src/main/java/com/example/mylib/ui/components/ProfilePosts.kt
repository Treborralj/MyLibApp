package com.example.mylib.ui.components

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
fun ProfilePosts(
    viewModel: ProfileViewModel,
    navController: NavController,
    username: String,
)
{
    val uiState by viewModel.uiState.collectAsState();

    val refreshPosts =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("refreshPosts", false)
            ?.collectAsState()

    LaunchedEffect(refreshPosts?.value) {
        if (refreshPosts?.value == true) {
            viewModel.fetchPosts(username)
            navController.currentBackStackEntry?.savedStateHandle?.set("refreshPosts", false)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        when {
            !uiState.postsError.isEmpty() -> {
                Text(
                    text = uiState.postsError,
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.loadingPosts -> {
                Text(
                    text = "Loading posts...",
                )
            }
            uiState.posts.isEmpty() && !uiState.loadingPosts -> {
                Text("This account has no posts yet")
            }

            true -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(
                        items = uiState.posts
                    ) { item ->
                        PostFrame(
                            username,
                            "Post Title",
                            content = PostReviewItem.PostItem(item.post)
                        )
                        Button(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("postId", item.post.id)
                            navController.currentBackStackEntry?.savedStateHandle?.set("postText", item.post.text)
                            navController.currentBackStackEntry?.savedStateHandle?.set("postTime", item.post.time)
                            navController.navigate(Routes.PostEditor.route)
                        }) {
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}