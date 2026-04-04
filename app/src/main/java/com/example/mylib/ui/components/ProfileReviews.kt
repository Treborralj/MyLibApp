package com.example.mylib.ui.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.ProfileViewModel

@Composable
fun ProfileReviews(
    viewModel: ProfileViewModel,
    navController: NavController,
    username: String,
) {
    val uiState by viewModel.uiState.collectAsState();
    val reviews by viewModel.uiReviewState.collectAsState()

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
            uiState.reviews.isEmpty() && !uiState.loading -> {
                Text("This account has no reviews yet")
            }

            true -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFFDF8F8)),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ){
                    items(
                        items = reviews
                    ) { item ->
                        if (item.review.username.isEmpty()) {

                        } else {
                            PostFrame(
                                username,
                                "Book Title",
                                content = item,
                                onEdit = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "reviewId",
                                        item.review.id
                                    )
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "reviewText",
                                        item.review.text
                                    )
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "reviewTime",
                                        item.review.time
                                    )
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "reviewScore",
                                        item.review.score
                                    )
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "reviewBookId",
                                        item.review.bookId
                                    )
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "reviewUsername",
                                        item.review.username
                                    )
                                    navController.navigate(Routes.ReviewEditor.route)
                                },
                                onClickUser = { u -> navController.navigate(Routes.Profile.route + "/" + u) },
                            )
                        }

                    }
                }
            }
        }
    }

}







@Preview
@Composable
fun ProfileReviewsPreview(
    //viewModel: ProfileViewModel,
   // navController: NavController,
    username:String =  "Sample User",
    reviewsError:String = "",
    loadingReviews:Boolean = false,
    reviews: List<ReviewResponse> = List(10,
        {
            ReviewResponse(it, username="Sample User",text ="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                time = "10-10-25", score = 3.5, bookId = it)
        }),
    ownProfile:Boolean = false,

) {
   // val uiState by viewModel.uiState.collectAsState();

   // val refreshReviews =
    //    navController.currentBackStackEntry
     //       ?.savedStateHandle
      //      ?.getStateFlow("refreshReviews", false)
       //     ?.collectAsState()

 //   LaunchedEffect(refreshReviews?.value) {
   //     if (refreshReviews?.value == true) {
      //      viewModel.fetchReviews(username)
    //        navController.currentBackStackEntry?.savedStateHandle?.set("refreshReviews", false)
     //   }
  //  }

    Card(
        modifier = Modifier.fillMaxSize()
    ){
        when {
            !reviewsError.isEmpty() -> {
                Text(
                    text = reviewsError,
                    color = MaterialTheme.colorScheme.error
                )
            }
            loadingReviews -> {
                Text(
                    text = "Loading reviews...",
                )
            }
            reviews.isEmpty() && !loadingReviews -> {
                Text("This account has no reviews yet")
            }

            true -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .background(Color(0xFFFDF8F8))
                    ,
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ){
                    items<ReviewResponse>(
                        items = reviews
                    ) { item ->
                            Column(){
                                PostFramePreview(

                                )
                            }


                    }
                }
            }
        }
    }

}