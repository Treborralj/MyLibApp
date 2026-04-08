package com.example.mylib.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylib.data.models.ReviewResponse

@Preview
@Composable
fun ReviewCardPreview(
    review: ReviewResponse = ReviewResponse(
        id = 0,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        time = "2026-02-25T15:58:58.107948",
        score = 3.5,
        bookId = 1,
        accountId = 0,
        username = "placeholder",
        bookTitle = "palceholder"
    ),
    bookTitle: String = "Sample Book"
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
                    text = bookTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                StarRating(
                    starSize = 20,
                    tint = Color(0xFF6650a4),
                    rating = review.score.toFloat(),
                    onRatingChange = {a: Float -> println("placeholder function")}
                )
            }

            review.text?.let { text ->
                //Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    modifier = Modifier
                        .padding(0.dp)
                    ,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                        ,
                        text = text,
                        style = MaterialTheme.typography.bodySmall

                    )
                }
            }

        }
    }
}


@Composable
fun ReviewCard(
    review: ReviewResponse,
    bookTitle: String = "Book Title"
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
                    text = bookTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                StarRating(
                    starSize = 20,
                    tint = Color(0xFF6650a4),
                    rating = review.score.toFloat(),
                    onRatingChange = {a: Float -> println("placeholder function")}
                )
            }

            review.text?.let { text ->
                //Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    modifier = Modifier
                        .padding(0.dp)
                    ,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                        ,
                        text = text,
                        style = MaterialTheme.typography.bodySmall

                    )
                }
            }

        }
    }
}