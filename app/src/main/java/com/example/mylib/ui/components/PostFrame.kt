package com.example.mylib.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylib.MainActivity
import com.example.mylib.R
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.viewModel.PostReviewItem


@Composable
fun PostFrame(
    username: String = "sampleuser", // þegar það er náð í feed þá gefur bakendinn ekki uppl. um það frá hverjum postarnir eru, gætum viljað breyta því
    bookTitle: String? = "Sample Book",
    //profilePic:
    //content: PostReviewItem,
    content: PostReviewItem,
    onEdit: (() -> Unit)? = null,
    onClickUser: (username:String) -> Unit,
) {

    var bitmap: Bitmap?;
    when (content) {
        is PostReviewItem.PostItem -> {
            bitmap = remember(content.post.profilePic){
                try{
                    content.post.profilePic?.let { base64String ->
                        val bytes = Base64.decode(base64String, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    }
                } catch (e: Exception){
                    null
                }
            }
        }
        is PostReviewItem.ReviewItem -> {
            bitmap = remember(content.review.profilePic){
                try{
                    content.review.profilePic?.let { base64String ->
                        val bytes = Base64.decode(base64String, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    }
                } catch (e: Exception){
                    null
                }
            }
        }
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(border=BorderStroke(
                width = 3.dp,
                color = Color(0xFF6650a4))),
    ) {
        Column(
            modifier = Modifier.padding(0.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            // verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(border=BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF6650a4)))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var name: String
                if (content is PostReviewItem.PostItem) {
                    name = content.post.username
                } else {
                    name = username
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = { onClickUser(name) }),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (bitmap == null) {
                        Surface {
                            Image(
                                painter = painterResource(R.drawable.profile_pic_placeholder),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    // .size(width = 56.dp, height = 79.dp)
                                    .size(width = 70.dp, height = 80.dp)
                                    .background(
                                        color = Color(0xFF6650a4),
                                        shape = RectangleShape
                                    )
                            )
                        }
                    } else {
                        Surface {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Profile Picture",
                                modifier = Modifier.size(width = 70.dp, height = 80.dp)
                            )
                        }
                    }

                    when (content) {
                        is PostReviewItem.PostItem -> {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                ,
                                text = content.post.username,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start
                            )
                        }
                        is PostReviewItem.ReviewItem -> {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                ,
                                text = username,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                }

                when (content) {
                    is PostReviewItem.PostItem -> {
                        Text(
                            text = content.post.time?.slice(IntRange(0,9)) ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    is PostReviewItem.ReviewItem -> {
                        Text(
                            text = content.review.time?.slice(IntRange(0,9)) ?:"",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }


            when (content) {
                is PostReviewItem.PostItem -> {
                    PostCard(post = content.post)
                }
                is PostReviewItem.ReviewItem -> {
                    ReviewCard(review = content.review)
                }
            }

            if (username == MainActivity.loggedInUser && onEdit != null) {
                TextButton(
                    shape = RoundedCornerShape(25),
                    onClick = {
                        onEdit()
                    }) {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun PostFramePreview(
    username: String = "sampleuser",
    bookTitle: String = "Sample Book",
    //profilePic:
    review: ReviewResponse = ReviewResponse(
        id =0,
        text ="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        time = "2026-02-25T15:58:58.107948",
        score =3.5,
        bookId = 1,
        username = "Sample User",
        profilePic = null,
    ),
    isReview:Boolean = true,

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(border=BorderStroke(
                width = 3.dp,
                color = Color(0xFF6650a4))),
    ) {
        Column(
            modifier = Modifier.padding(0.dp),
            //verticalArrangement = Arrangement.SpaceBetween
           // verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(border=BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF6650a4)))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f)
                    ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                        Image(
                            painter = painterResource(R.drawable.profile_pic_placeholder),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                // .size(width = 56.dp, height = 79.dp)
                                .size(width = 70.dp, height = 80.dp)
                                .background(
                                    color = Color(0xFF6650a4),
                                    shape = RectangleShape
                                )
                        )



                    Text(
                        modifier = Modifier
                            .weight(1f)
                        ,
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )


                }


                Text(
                    text = review.time?.slice(IntRange(0,9)) ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


                when {
                    isReview -> {
                        ReviewCardPreview()
                    }
                    !isReview -> {
                        PostCardPreview()
                    }
                }



            if (true) {
                TextButton(
                    shape = RoundedCornerShape(25),
                    onClick = {

                    }) {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }




        }
    }
}





