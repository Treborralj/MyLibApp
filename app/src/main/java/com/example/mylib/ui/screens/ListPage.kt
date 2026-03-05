package com.example.mylib.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mylib.data.models.BookResponse
import com.example.mylib.viewModel.Lists.ListType
import com.example.mylib.viewModel.Lists.ListViewModel


@Composable
fun ListPage(
    listViewModel: ListViewModel
){

    val state by listViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { listViewModel.loadLists() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        if(state.isLoading){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                CircularProgressIndicator()
            }
            Spacer(Modifier.height(16.dp))
        }
        state.error?.let{
            Text(text = it)
            Spacer(Modifier.height(16.dp))
        }

        ExpandableListSection(
            title = ListType.WANT_TO_READ.title,
            expanded = state.expanded.contains(ListType.WANT_TO_READ),
            onToggle = { listViewModel.toggle(ListType.WANT_TO_READ) },
            books = state.wantToRead,
            onRemove = { bookId ->
                listViewModel.removeFromList(ListType.WANT_TO_READ, bookId)
            }
        )

        Spacer(Modifier.height(12.dp))

        ExpandableListSection(
            title = ListType.AM_READING.title,
            expanded = state.expanded.contains(ListType.AM_READING),
            onToggle = { listViewModel.toggle(ListType.AM_READING) },
            books = state.amReading,
            onRemove = { bookId ->
                listViewModel.removeFromList(ListType.AM_READING, bookId)
            }
        )

        Spacer(Modifier.height(12.dp))

        ExpandableListSection(
            title = ListType.HAVE_READ.title,
            expanded = state.expanded.contains(ListType.HAVE_READ),
            onToggle = { listViewModel.toggle(ListType.HAVE_READ) },
            books = state.haveRead,
            onRemove = { bookId ->
                listViewModel.removeFromList(ListType.HAVE_READ, bookId)
            }
        )
    }
}
@Composable
private fun ExpandableListSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    books: List<BookResponse>,
    onRemove: (bookId: Int) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ onToggle() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = if (expanded) "▼ $title" else "► $title")
        }

        HorizontalDivider()

        AnimatedVisibility(visible = expanded) {
            if(books.isEmpty()){
                Text(
                    text = "No Books In Your List",
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp)
                        .padding(top = 8.dp)
                ) {
                    items(books, key = { it.id }){ book ->
                        BookRow(
                            title = book.name ?: "Untitled",
                            onRemove = { onRemove(book.id)}
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookRow(
    title: String,
    onRemove: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = title, modifier = Modifier.weight(1f))
        IconButton(onClick = onRemove){
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Remove Book")
        }
    }
}