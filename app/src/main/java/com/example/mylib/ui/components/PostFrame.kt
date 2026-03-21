package com.example.mylib.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylib.R
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.viewModel.PostReviewItem

@Preview
@Composable
fun PostFramePreview(
    username: String = "sampleuser",
    bookTitle: String = "Sample Book",
    //profilePic:
    review: ReviewResponse = ReviewResponse(
        id = 0,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        time = "2026-02-25T15:58:58.107948",
        score = 3.5,
        bookId = 1,
        username = "sampleuser",
        accountId = 0
    ),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            //verticalArrangement = Arrangement.SpaceBetween
           // verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    Image(
                        painter = painterResource(R.drawable.profile_pic_placeholder),
                        contentDescription = "Book cover",
                        modifier = Modifier.size(width = 56.dp, height = 79.dp)
                    )
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }


                Text(
                    text = review.time?.slice(IntRange(0,9)) ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        ReviewCardPreview(review, bookTitle)


        }
    }
}




@Composable
fun PostFrame(
    username: String = "sampleuser", // þegar það er náð í feed þá gefur bakendinn ekki uppl. um það frá hverjum postarnir eru, gætum viljað breyta því
    bookTitle: String? = "Sample Book",
    //profilePic:
    content: PostReviewItem,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            // verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    Image(
                        painter = painterResource(R.drawable.profile_pic_placeholder),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(width = 56.dp, height = 79.dp)
                    )
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }

                when(content) {
                    is PostReviewItem.PostItem -> Text(
                        text = content.post.time?.slice(IntRange(0,9)) ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    is PostReviewItem.ReviewItem -> Text(
                        text = content.review.time?.slice(IntRange(0,9)) ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            when(content) {
                is PostReviewItem.PostItem -> PostCard(content.post)
                is PostReviewItem.ReviewItem -> ReviewCard(content.review, bookTitle)
            }



        }
    }
}
