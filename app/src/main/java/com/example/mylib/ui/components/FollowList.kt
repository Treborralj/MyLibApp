package com.example.mylib.ui.components

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.mylib.data.models.FollowResponse
import com.example.mylib.viewModel.ProfileViewModel
import com.example.mylib.viewModel.search.SearchItem
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Icon
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.IconButton



import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces

enum class followListType(val title: String) {
    FOLLOWERS("Followers"),
    FOLLOWING("Following")
}


@Composable
fun FollowList(
    viewModel: ProfileViewModel,
    type: followListType,
) {
    val uiState by viewModel.uiState.collectAsState();


    var accounts: List<FollowResponse> = emptyList();
    if (type == followListType.FOLLOWERS) {
        accounts = uiState.profileData?.followers!!
    }
    if (type == followListType.FOLLOWING) {
        accounts = uiState.profileData?.following!!
    }



    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                    .fillMaxHeight(0.20f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = type.title,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontSize = 45.sp,
                )
                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    //shape = RoundedCornerShape(35),
                    onClick = {
                        if (type == followListType.FOLLOWERS) {
                            viewModel.setViewingFollowers(false)
                        } else {
                            viewModel.setViewingFollowing(false)
                        }
                    },
                    content = {
                        Icon(
                            modifier = Modifier.size(60.dp),
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF6650a4),
                        )
                    }
                )


            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ){
                items(
                    items = accounts,
                ){ item ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillParentMaxHeight(0.15f)
                            .fillParentMaxWidth(0.90f)
                            .border(border=BorderStroke(
                                width = 3.dp,
                                color = Color(0xFF6650a4)))
                            .padding(10.dp),
                    ) {
                        FollowListItem(item);
                    }

                }
            }
        }
    }
}







@Preview
@Composable
fun FollowListPreview(
    //viewModel: ProfileViewModel,
    type: followListType = followListType.FOLLOWERS,
    accounts: List<FollowResponse> = listOf(FollowResponse("Sample User 1"),FollowResponse("Sample User 2"),FollowResponse("Sample User 3"),
        FollowResponse("Sample User 4"),FollowResponse("Sample User 5")),
) {
    //val uiState by viewModel.uiState.collectAsState();

    Card(
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                    .fillMaxHeight(0.20f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = type.title,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    fontSize = 45.sp,
                )
                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    //shape = RoundedCornerShape(35),
                    onClick = {

                    },
                    content = {
                        Icon(
                            modifier = Modifier.size(60.dp),
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF6650a4),
                        )
                    }
                )


            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ){
                items(
                    items = accounts,
                ){ item ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.fillParentMaxHeight(0.15f)
                             .fillParentMaxWidth(0.90f)
                            .border(border=BorderStroke(
                                width = 3.dp,
                                color = Color(0xFF6650a4)))
                            .padding(10.dp),
                    ) {
                        FollowListItemPreview();
                    }
                    
                }
            }
        }
    }
}