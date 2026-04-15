//package com.example.mylib.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.ui.navigation.Routes
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

    Card(
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
                    modifier = Modifier.fillMaxSize()
                        .background(Color(0xFFFDF8F8)),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ){
                    items(
                        items = uiState.posts
                    ) { item ->
                        val postUsername = item.post.username

                        LaunchedEffect(postUsername) {
                            viewModel.resolveProfilePicture(postUsername)
                        }

                        PostFrame(
                            username = postUsername,
                            content = item,
                            profilePicPath = viewModel.profilePictures[postUsername],
                            onEdit = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("postId", item.post.id)
                                navController.currentBackStackEntry?.savedStateHandle?.set("postText", item.post.text)
                                navController.currentBackStackEntry?.savedStateHandle?.set("postTime", item.post.time)
                                navController.navigate(Routes.PostEditor.route)
                            },
                            onClickUser = {u -> navController.navigate(Routes.Profile.route + "/" + u)}
                        )
                    }
                }
            }
        }
    }
}
