package com.example.mylib.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mylib.data.models.BookResponse
import com.example.mylib.ui.components.BookListItem
import com.example.mylib.viewModel.BookSearchViewModel

@Composable
fun BookSearchPage(
    viewModel: BookSearchViewModel,
    onBookClick: (BookResponse) -> Unit = {}
){
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        OutlinedTextField(
            value = uiState.queryString,
            onValueChange = viewModel::onQueryStringChange,
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Search books")},
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        if(uiState.loading){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        uiState.error?.let{ msg ->
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(uiState.results, key = { it.id!! }){ book ->
                BookListItem(
                    book = book,
                    onClick = {onBookClick(book)}
                )
            }
        }

    }
}