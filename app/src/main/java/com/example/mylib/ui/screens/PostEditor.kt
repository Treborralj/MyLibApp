package com.example.mylib.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.data.models.PostResponse
import com.example.mylib.viewModel.PostEditorViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import com.example.mylib.util.createImageUri
import coil.compose.AsyncImage
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun PostEditor(
    viewModel: PostEditorViewModel,
    post: PostResponse? = null,
    navController: NavController,
)
{
    val uiState by viewModel.uiState.collectAsState()
    val isEditing = post != null && post.id != 0

    var newTitle by rememberSaveable(post?.id) {
        mutableStateOf(if (isEditing) post?.title ?: "" else "")
    }
    var newText by rememberSaveable(post?.id) {
        mutableStateOf(if (isEditing) post?.text ?: "" else "")
    }

    val context = LocalContext.current
    var selectedImageUri by rememberSaveable {mutableStateOf<Uri?>(null)}
    var cameraImageUri by remember {mutableStateOf<Uri?>(null)}

    val backendBitmap = remember(post?.imageBase64) {
        post?.imageBase64?.let {
            try {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }

    LaunchedEffect(uiState.result, uiState.error) {
        if (uiState.result != null && uiState.error.isEmpty()) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("refreshPosts", true)

            navController.popBackStack()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if(uri != null){
            selectedImageUri = uri
        }
    }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success){
            selectedImageUri = cameraImageUri
    }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .padding(12.dp)
                .padding(top = 5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
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

                if (isEditing) {
                    IconButton(
                        onClick = {
                            viewModel.deletePost(post!!.id)
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
                Text(
                    modifier = Modifier.padding(),
                    text = if (isEditing) "Edit Post" else "Create Post",
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }

            when {
                uiState.loading -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
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
                            .fillMaxWidth()
                            .padding(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            placeholder = { Text("Title") },
                        )

                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        value = newText,
                        onValueChange = { newText = it },
                        placeholder = { Text("Write your text here...")},
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Button(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ){
                            Text("Choose Photo")
                        }
                        Button(
                            onClick = {
                                val uri = createImageUri(context)
                                cameraImageUri = uri
                                takePictureLauncher.launch(uri)
                            },
                            modifier = Modifier.weight(1f)
                        ){
                            Text("Take Photo")
                        }

                    }
                    if (selectedImageUri != null) {
                        Spacer(modifier = Modifier.height(12.dp))

                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected post image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { selectedImageUri = null }
                        ) {
                            Text("Remove Photo")
                        }
                    } else if (backendBitmap != null) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Image(
                            bitmap = backendBitmap.asImageBitmap(),
                            contentDescription = "Existing post image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                    }

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {

                        Button(
                            onClick = {
                                val id = if (isEditing) post?.id else null
                                viewModel.editPost(
                                    title = newTitle,
                                    text = newText.trim(),
                                    imageUri = selectedImageUri,
                                    id = id
                                )
                            },
                            enabled = newTitle.isNotBlank() || newText.isNotBlank() || selectedImageUri != null
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
        id =1,
        username = "Bob",
        title = "Wow",
        text ="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        time ="10-10-25",
        imageBase64 = null,
        imageType = null,
        profilePic = null,
    ),
    postTitle: String = "Title",
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
            modifier = Modifier
                .padding(12.dp)
                .padding(top = 5.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
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
                        .fillMaxWidth()
                        .padding(),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = newText,
                    onValueChange = { newText = it },
                    placeholder = { if(!isNewPost) {
                        post.text?.let { Text(text = it) }
                    } else {Text(text = "Write your text here...")} },
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