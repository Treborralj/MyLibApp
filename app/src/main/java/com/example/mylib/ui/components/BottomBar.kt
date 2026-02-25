package com.example.mylib.ui.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomBar(
    currentRoute: String?,
    onTabClick: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "homeFeedPage",
            onClick = { onTabClick("homeFeedPage") },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = currentRoute == "bookSearchPage",
            onClick = { onTabClick("bookSearchPage") },
            icon = { Icon(Icons.Filled.Search, contentDescription = null) },
            label = { Text("Search") }
        )

    }
}