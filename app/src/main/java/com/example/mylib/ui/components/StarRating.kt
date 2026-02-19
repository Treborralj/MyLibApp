package com.example.mylib.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(
    rating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Int = 28
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            val icon = when {
                rating >= i -> Icons.Filled.Star
                rating >= i - 0.5f -> Icons.Filled.StarHalf
                else -> Icons.Outlined.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = "Rate $i stars",
                modifier = Modifier
                    .size(starSize.dp)
                    .clickable {
                        val full = i.toFloat()
                        val half = i - 0.5f
                        val emptyThisStar = (i - 1).toFloat()

                        val newRating = when (rating) {
                            full -> half
                            half -> emptyThisStar
                            else -> full
                        }

                        onRatingChange(newRating)
                    }
            )
        }
    }
}
