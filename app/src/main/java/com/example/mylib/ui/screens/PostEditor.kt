package com.example.mylib.ui.screens

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.mylib.MainActivity
import com.example.mylib.R
import com.example.mylib.data.models.PostResponse
import com.example.mylib.viewModel.PostEditorViewModel
import com.example.mylib.viewModel.authentication.AuthenticationViewModel

@Composable
fun PostEditor(
    //onUploadPic: () -> Unit,
    viewModel: PostEditorViewModel,
    post: PostResponse?,
    postTitle: String = "Title" ,// það er reyndar ekki titill á postum í bakendanum, gætum viljað bæta því við
    navController: NavController,
)
{

    var newTitle by rememberSaveable { mutableStateOf("") }
    var newText by rememberSaveable { mutableStateOf("") }

    // Observe state
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.result, uiState.error) {
        if (uiState.result != null && uiState.error == null) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("refreshPosts", true)

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
                    placeholder = { Text(text = postTitle) },
                )

                Image(
                    painter = painterResource(R.drawable.post_pic_placeholder),
                    contentDescription = "Post Image",
                    //modifier = Modifier.size(width = 112.dp, height = 158.dp)
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = newText,
                onValueChange = { newText = it },
                placeholder = { if(post?.text != null) {Text(text = post.text)} else {Text(text = "Write your text here...")} },
            )

            Row () {
                Button(
                    onClick = {
                        if (post?.id !=null) {
                            viewModel.editPost(newText,post.id)
                        } else {
                            viewModel.editPost(newText,null)
                        }
                        if (!uiState.loading && uiState.error == null) {
                            navController.popBackStack();
                        }
                    }
                ){
                    Text("Save")
                }
                if (post?.id!=null) {
                    Button(
                        onClick = {
                            viewModel.deletePost(post.id)
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