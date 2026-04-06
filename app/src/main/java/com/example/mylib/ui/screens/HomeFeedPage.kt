package com.example.mylib.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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
import androidx.compose.runtime.remember
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
    val posts by viewModel.uiPostState.collectAsState()
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
            !posts.isEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ){
                    items(
                        posts,
                    ){ item ->
                        if (!item.post.username.isEmpty()) {
                            PostFrame("Sample User", "post title", content = item,
                                onClickUser = {u -> navController.navigate(Routes.Profile.route + "/" + u)},
                            )
                        }
                    }
                }
            }
            posts.isEmpty() -> {
                Text("No new posts found")
            }
        }
    }

}