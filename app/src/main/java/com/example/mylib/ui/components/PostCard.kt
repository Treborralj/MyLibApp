package com.example.mylib.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylib.R
import com.example.mylib.data.models.PostResponse

@Composable
fun PostCard(
    post: PostResponse,
) {
    val bitmap = remember(post.imageBase64){
        try{
            post.imageBase64?.let { base64String ->
                val bytes = Base64.decode(base64String, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
        } catch (e: Exception){
            null
        }
    }
    Card(
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            post.title?.takeIf {it.isNotBlank() }?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = post.text,
                style = MaterialTheme.typography.bodySmall
            )
            if(bitmap != null){
                Surface {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Post Image",
                        modifier = Modifier.fillMaxWidth().height(220.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PostCardPreview(
    post: PostResponse = PostResponse(
        id =0,
        username = "Bob",
        title = "Sample post",
        text ="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        time = "2026-02-25T15:58:58.107948",
        imageBase64 = null,
        imageType = null
    ),
    postTitle: String = "Sample Post"
)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier.padding(12.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = postTitle,
                    style = MaterialTheme.typography.titleMedium,
                    // maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }

            post.text?.let { text ->
                //Spacer(modifier = Modifier.height(4.dp))
                Surface() {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = text,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth()
                //     .wrapContentSize(Alignment.Center)
                ,
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.post_pic_placeholder),
                    contentDescription = "Post Image",
                    //modifier = Modifier.size(width = 112.dp, height = 158.dp)
                )
            }


        }
    }
}