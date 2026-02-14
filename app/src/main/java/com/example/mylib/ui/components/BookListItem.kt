package com.example.mylib.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mylib.data.models.BookResponse

@Composable
fun BookListItem(
    book: BookResponse,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
    ){
       Column(modifier = Modifier.padding(12.dp)){
           Text(
               text = book.name ?: "No title",
               style = MaterialTheme.typography.titleMedium,
               maxLines = 1,
               overflow = TextOverflow.Ellipsis
           )
           val subtitleParts = listOfNotNull(
               book.writer?.takeIf{it.isNotBlank()},
               book.genre?.takeIf { it.isNotBlank() }
           )

           if(subtitleParts.isNotEmpty()){
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                   text = subtitleParts.joinToString(" • "),
                   style = MaterialTheme.typography.bodyMedium,
                   maxLines = 1,
                   overflow = TextOverflow.Ellipsis
               )
           }

           book.score?.let { score ->
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                   text = "Score: $score",
                   style = MaterialTheme.typography.bodySmall
               )
           }
       }
    }
}