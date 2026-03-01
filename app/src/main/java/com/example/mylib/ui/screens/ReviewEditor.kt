package com.example.mylib.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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

@Composable
fun ReviewEditor(
    //onUploadPic: () -> Unit,
    viewModel: ReviewEditorViewModel,
    review: ReviewResponse?,
    bookTitle: String?,// það fylgja reyndar ekki uppl. um bók með reviews í bakendanum, gætum viljað bæta því við
    bookId: Int?,
    navController: NavController,
)
{

    var newTitle by rememberSaveable { mutableStateOf("") }
    var newText by rememberSaveable { mutableStateOf("") }
    var newScore by rememberSaveable {mutableStateOf("0.0")}

    // Observe state
    val uiState by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(12.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            uiState.error?.let{ msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if(uiState.loading){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    CircularProgressIndicator()
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        placeholder = { if(bookTitle != null) {Text(text = bookTitle)} else {Text(text = "Enter Book Title...")} },
                    )



                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = newScore,
                    onValueChange = { newScore = it },
                    placeholder = { if(review?.score != null) {Text(text = review.score.toString())} else {Text(text = "Enter Score between 0 and 5 (e.g. 3.5)")} },
                    )

            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = newText,
                onValueChange = { newText = it },
                placeholder = { if(review?.text != null) {Text(text = review.text)} else {Text(text = "Write your text here...")} },
            )

            Row () {
                Button(
                    onClick = {
                        if (review?.id !=null) {
                            viewModel.editReview(newText,review.id,newScore.toDouble(),bookId)
                        } else {
                            viewModel.editReview(newText,null,newScore.toDouble(),bookId)
                        }
                        if (!uiState.loading && uiState.error == null) {
                            navController.popBackStack();
                        }
                    }
                ){
                    Text("Save")
                }

                if (review?.id!=null) {
                    Button(
                        onClick = {
                            viewModel.deleteReview(review.id);
                            if (!uiState.loading && uiState.error == null) {
                                navController.popBackStack();
                            }
                        }
                    ){
                        Text("Delete")
                    }
                }

            }


        }
    }
}