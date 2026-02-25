package com.example.mylib.ui.screens

import android.R.attr.label
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.UserResponse
import com.example.mylib.ui.components.BookListItem
import com.example.mylib.ui.components.UserListItem
import com.example.mylib.viewModel.search.BookSearchBy
import com.example.mylib.viewModel.search.SearchFor
import com.example.mylib.viewModel.search.SearchItem
import com.example.mylib.viewModel.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(
    viewModel: SearchViewModel,
    onBookClick: (BookResponse) -> Unit,
    onUserClick: (UserResponse) -> Unit
){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllBooks()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            SearchForDropDown(
                value = uiState.searchFor,
                onChange = viewModel::setSearchFor,
                modifier = Modifier.weight(1f)
            )
            if(uiState.searchFor == SearchFor.BOOKS){
                BookSearchByDropdown(
                    value = uiState.bookSearchBy,
                    onChange = viewModel::setBookSearchBy,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.queryString,
            onValueChange = viewModel::onQueryStringChange,
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Search")},
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
            items(
                uiState.results,
                key = { item ->
                    when(item){
                        is SearchItem.BookItem -> "book-${item.book.id ?: 0L}"
                        is SearchItem.UserItem -> "book-${item.user.id}"
                }
            }
            ){ item ->
                when(item){
                    is SearchItem.BookItem -> {
                        BookListItem(
                            book = item.book,
                            onClick = {onBookClick(item.book)}
                        )
                    }
                    is SearchItem.UserItem -> {
                        UserListItem(
                            user = item.user,
                            onClick = {onUserClick(item.user)}
                        )
                    }

                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchForDropDown(
    value: SearchFor,
    onChange: (SearchFor) -> Unit,
    modifier: Modifier = Modifier
){
    var expanded by remember {mutableStateOf(false)}
    val options = SearchFor.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = when(value){
                SearchFor.BOOKS -> "Books"
                SearchFor.USERS -> "Users"
            },
            onValueChange = {},
            label = { Text("Search for")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(if (option == SearchFor.BOOKS) "Books" else "Users")},
                    onClick = {
                        expanded = false
                        onChange(option)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookSearchByDropdown(
    value: BookSearchBy,
    onChange: (BookSearchBy) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val options = BookSearchBy.entries

    fun label(v: BookSearchBy) = when (v) {
        BookSearchBy.TITLE -> "Title"
        BookSearchBy.AUTHOR -> "Author"
        BookSearchBy.GENRE -> "Genre"
        BookSearchBy.ID -> "ID"
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = label(value),
            onValueChange = {},
            label = { Text("Search by") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(label(option)) },
                    onClick = {
                        expanded = false
                        onChange(option)
                    }
                )
            }
        }
    }
}