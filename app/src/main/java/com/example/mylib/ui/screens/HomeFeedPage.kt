package com.example.mylib.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.mylib.ui.components.PostFrame
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.BookViewModel
import com.example.mylib.viewModel.HomefeedViewModel
import com.example.mylib.viewModel.PostReviewItem

@Composable
fun HomeFeedPage(
    navController: NavController,
    viewModel: HomefeedViewModel
    ){
    // Observe state
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = MainActivity.bearerToken) {
        viewModel.fetchFeed()
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ){
        when {
            uiState.loading -> {
                Text("Loading posts...")
            }
            !uiState.posts.isEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ){
                    items(
                        uiState.posts,
                    ){ item ->

                        PostFrame(
                            username = item.post.username,
                            content = item,
                            onClickUser = {u -> navController.navigate(Routes.Profile.route + "/" + u)},
                        )

                    }
                }
            }
            uiState.posts.isEmpty() -> {
                Text("No new posts found")
            }
        }
    }

}