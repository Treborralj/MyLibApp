package com.example.mylib.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.mylib.MainActivity
import com.example.mylib.R
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.ui.components.PostFrame
import com.example.mylib.ui.components.ProfileHeader
import com.example.mylib.ui.components.ProfilePosts
import com.example.mylib.ui.components.ProfileReviews
import com.example.mylib.ui.components.ReviewCardPreview
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
        viewModel.fetchProfile(username)
        //viewModel.fetchPosts(username)
        //viewModel.fetchReviews(username,false)
    }

    ProfileHeader(viewModel)

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

                ProfilePosts(viewModel,navController,username)

            }

            uiState.viewingReviews -> {
                Button(onClick = { viewModel.fetchPosts(username) }) {
                    Text("See Posts")
                }
                Text(
                    text = "Reviews",
                    style = MaterialTheme.typography.headlineMedium
                )

                ProfileReviews(viewModel,navController,username)

            }
        }


    }


}