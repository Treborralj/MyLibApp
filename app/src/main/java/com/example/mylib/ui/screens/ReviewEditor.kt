package com.example.mylib.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.viewModel.ReviewEditorViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.mylib.ui.components.StarRating

@Composable
fun ReviewEditor(
    //onUploadPic: () -> Unit,
    viewModel: ReviewEditorViewModel,
    review: ReviewResponse?,
    bookTitle: String,// það fylgja reyndar ekki uppl. um bók með reviews í bakendanum, gætum viljað bæta því við
    bookId: Int?,
    navController: NavController,
)
{

    var newText by rememberSaveable(review?.id) {
        mutableStateOf(review?.text ?: "")
    }

    // var newScore by rememberSaveable(review?.id) {
    //     mutableStateOf((review?.score ?: 0.0).toString())
    //  }

    var newScore by rememberSaveable(review?.id) {
        mutableStateOf((review?.score ?: 0.0))
    }


     //Observe state
     val uiState by viewModel.uiState.collectAsState()
      LaunchedEffect(uiState.result, uiState.error) {
          if (uiState.result != null && uiState.error.isEmpty()) {
             navController.previousBackStackEntry
               ?.savedStateHandle
               ?.set("refreshReviews", true)

          navController.popBackStack()
      }
      }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(12.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){

            when {
                !uiState.error.isEmpty() -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = uiState.error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                uiState.loading -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        CircularProgressIndicator()
                    }
                }

                true -> {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            content = {
                                Icon(
                                    modifier = Modifier.size(30.dp),
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Go Back",
                                    tint = Color(0xFF6650a4),
                                )
                            }
                        )

                        if (review != null) {
                            IconButton(
                                onClick = {
                                    viewModel.deleteReview(review.id);
                                    navController.popBackStack()
                                },
                                content = {
                                    Icon(
                                        modifier = Modifier.size(30.dp),
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Delete Review",
                                        tint = Color(0xFF6650a4),
                                    )
                                }
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            Text(
                                modifier = Modifier.padding(),
                                text = bookTitle,
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            StarRating(
                                starSize = 50,
                                tint = Color(0xFF6650a4),
                                rating = newScore.toFloat(),
                                onRatingChange =  { stars: Float ->
                                    newScore = stars.toDouble()
                                }
                            )
                        }

                        //       OutlinedTextField(
                        //           modifier = Modifier.fillMaxWidth(),
                        //          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        //          value = newScore,
                        //            onValueChange = { newScore = it },
                        //            placeholder = { if(review?.score != null) {Text(text = review.score.toString())} else {Text(text = "Enter Score between 0 and 5 (e.g. 3.5)")} },
                        //       )

                    }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        value = newText,
                        onValueChange = { newText = it },
                        placeholder = { if(review?.text != null) {Text(text = review.text)} else {Text(text = "Write your text here...")} },
                    )

                    Row (
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                   val score = newScore;

                                     if (review != null) {
                                       viewModel.editReview(text=newText, id=review.id, score=score, bookId=review.bookId)
                                  } else {
                                      viewModel.editReview(text=newText, score=score, bookId = bookId)
                                  }
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}









@Preview
@Composable
fun ReviewEditorPreview(
    //onUploadPic: () -> Unit,
   // viewModel: ReviewEditorViewModel,
    review: ReviewResponse? = ReviewResponse(1,text="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        "10-10-25",3.5,1),
    bookTitle: String = "Title",// það fylgja reyndar ekki uppl. um bók með reviews í bakendanum, gætum viljað bæta því við
    bookId: Int? = 1,
    //navController: NavController,
    isNewReview:Boolean = false,
    error:String = "",
    loading:Boolean = false,
)
{

    var newText by rememberSaveable(review?.id) {
        mutableStateOf(review?.text ?: "")
    }

   // var newScore by rememberSaveable(review?.id) {
   //     mutableStateOf((review?.score ?: 0.0).toString())
  //  }

    var newScore by rememberSaveable(review?.id) {
             mutableStateOf((review?.score ?: 0.0).toFloat())
          }


    // Observe state
   // val uiState by viewModel.uiState.collectAsState()
  //  LaunchedEffect(uiState.result, uiState.error) {
  //      if (uiState.result != null && uiState.error.isEmpty()) {
   //         navController.previousBackStackEntry
     //           ?.savedStateHandle
     //           ?.set("refreshReviews", true)

      //      navController.popBackStack()
      //  }
  //  }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(12.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            if(!error.isEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if(loading){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    CircularProgressIndicator()
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = {

                    },
                    content = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back",
                            tint = Color(0xFF6650a4),
                        )
                    }
                )

                IconButton(
                    onClick = {
                        //    viewModel.deleteReview(review.id);
                    },
                    content = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Review",
                            tint = Color(0xFF6650a4),
                        )
                    }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.padding(),
                        text = bookTitle,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    StarRating(
                        starSize = 50,
                        tint = Color(0xFF6650a4),
                        rating = newScore,
                        onRatingChange =  {

                        }
                    )
                }


         //       OutlinedTextField(
         //           modifier = Modifier.fillMaxWidth(),
          //          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          //          value = newScore,
        //            onValueChange = { newScore = it },
        //            placeholder = { if(review?.score != null) {Text(text = review.score.toString())} else {Text(text = "Enter Score between 0 and 5 (e.g. 3.5)")} },
         //       )

            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().weight(1f),
                value = newText,
                onValueChange = { newText = it },
                placeholder = { if(review?.text != null) {Text(text = review.text)} else {Text(text = "Write your text here...")} },
            )

            Row (
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                     //   val score = newScore.toDoubleOrNull() ?: 0.0

                   //     if (review?.id != null) {
                     //       viewModel.editReview(newText, review.id, score, bookId)
                      //  } else {
                      //      viewModel.editReview(newText, null, score, bookId)
                      //  }
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}