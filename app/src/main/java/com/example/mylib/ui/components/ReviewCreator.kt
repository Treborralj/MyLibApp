package com.example.mylib.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReviewCreatorDialog(
    open: Boolean,
    bookTitle: String,
    initialRating: Float = 0f,
    initialText: String = "",
    onDismiss: () -> Unit,
    onSubmit: (rating: Float, text: String) -> Unit
) {
    if (!open) return

    var rating by remember { mutableFloatStateOf(initialRating) }
    var text by remember { mutableStateOf(initialText) }

    LaunchedEffect(open) {
        if (open) {
            rating = initialRating
            text = initialText
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Write a review") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(bookTitle)

                Text("Your rating: ${rating} / 5")

                StarRating(
                    rating = rating,
                    onRatingChange = { rating = it }
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Your review") },
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(rating, text.trim()) },
                enabled = rating > 0f && text.trim().isNotEmpty()
            ) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}