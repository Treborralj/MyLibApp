package com.example.mylib.ui.screens

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.mylib.MainActivity
import com.example.mylib.R
import com.example.mylib.data.models.PostResponse
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.PostEditorViewModel
import com.example.mylib.viewModel.authentication.AuthenticationViewModel

@Composable
fun PostEditor(
    //onUploadPic: () -> Unit,
    viewModel: PostEditorViewModel,
    post: PostResponse? = null,
    postTitle: String = "Title" ,// það er reyndar ekki titill á postum í bakendanum, gætum viljað bæta því við
    navController: NavController,
)
{

    var newTitle by rememberSaveable { mutableStateOf("") }
    var newText by rememberSaveable { mutableStateOf("") }

     //Observe state
     val uiState by viewModel.uiState.collectAsState()
      LaunchedEffect(uiState.result, uiState.error) {
         if (uiState.result != null && uiState.error.isEmpty()) {
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
            modifier = Modifier.padding(12.dp).padding(top=5.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){

            Row(
                modifier = Modifier.fillMaxWidth().height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack();
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

                if (post != null) {
                    IconButton(
                        onClick = {
                            viewModel.deletePost(post.id);
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Post",
                                tint = Color(0xFF6650a4),
                            )
                        }
                    )
                }

            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (post == null) {
                    Text(
                        modifier = Modifier.padding(),
                        text = "Create Post",
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(),
                        text = "Edit Post",
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            when {
                uiState.loading -> {
                    Row(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        horizontalArrangement = Arrangement.Center
                    ){
                        CircularProgressIndicator()
                    }
                }

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

                true -> {
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

                    }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        value = newText,
                        onValueChange = { newText = it },
                        placeholder = { if(post!=null) {Text(text = post.text)} else {Text(text = "Write your text here...")} },
                    )

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {

                        Button(
                            onClick = {
                                val id = post?.id; // ef þetta er nýr post þá er id null og þá být editPost aðferðin til nýjan
                                viewModel.editPost(newText,id);
                            }
                        ){
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
fun PostEditorPreview(
    //onUploadPic: () -> Unit,
    //viewModel: PostEditorViewModel,
    post: PostResponse = PostResponse(
        id=1,
        text="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        time="10-10-25"),
    postTitle: String = "Title",// það er reyndar ekki titill á postum í bakendanum, gætum viljað bæta því við
    //navController: NavController,
    //error: String = "Could not save changes",
    error: String = "",
    loading: Boolean = false,
    isNewPost: Boolean = true,
)
{

    var newTitle by rememberSaveable { mutableStateOf("") }
    var newText by rememberSaveable { mutableStateOf("") }

    // Observe state
   // val uiState by viewModel.uiState.collectAsState()
  //  LaunchedEffect(uiState.result, uiState.error) {
   //     if (uiState.result != null && uiState.error == null) {
    //        navController.previousBackStackEntry
     //           ?.savedStateHandle
      //          ?.set("refreshPosts", true)

     //       navController.popBackStack()
      //  }
   // }

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(12.dp).padding(top=5.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){

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

                    },
                    content = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Post",
                            tint = Color(0xFF6650a4),
                        )
                    }
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isNewPost) {
                    Text(
                        modifier = Modifier.padding(),
                        text = "Create Post",
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(),
                        text = "Edit Post",
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            if(loading){
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.Center
                ){
                    CircularProgressIndicator()
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            else {
                if (!error.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
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

            }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    value = newText,
                    onValueChange = { newText = it },
                    placeholder = { if(!isNewPost) {Text(text = post.text)} else {Text(text = "Write your text here...")} },
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Button(
                        onClick = {
                            //     if (post?.id !=null) {
                            //       viewModel.editPost(newText,post.id)
                            //  } else {
                            //      viewModel.editPost(newText,null)
                            //   }
                            //    if (!uiState.loading && uiState.error == null) {
                            //        navController.popBackStack();
                            //   }
                        }
                    ){
                        Text("Save")
                    }



                }
            }




        }
    }
}