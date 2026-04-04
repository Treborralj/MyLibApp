package com.example.mylib.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mylib.MainActivity
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.ui.components.FollowList
import com.example.mylib.ui.components.FollowListPreview
import com.example.mylib.ui.components.ProfileHeader
import com.example.mylib.ui.components.ProfileHeaderPreview
import com.example.mylib.ui.components.ProfilePosts
import com.example.mylib.ui.components.ProfileReviews
import com.example.mylib.ui.components.ProfileReviewsPreview
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.ProfileViewModel

@Composable
fun ProfilePage(
    username: String,
    navController: NavController,
    viewModel: ProfileViewModel,
){
    //Observe state
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(username) {
        viewModel.fetchProfile(username);
    }

    class CollapsingHeaderNestedScrollConnection(
    ) : NestedScrollConnection {

        var neutralPosition: Int by mutableIntStateOf(0)

        var headerHeight: Int by mutableIntStateOf(0)

        var headerOffset: Int by mutableIntStateOf(0)
            private set

        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y.toInt()
            val newOffset = headerOffset + delta
            val previousOffset = headerOffset
            if (newOffset > previousOffset) {
                return Offset(x=0f, y=0f)
            }
            headerOffset = newOffset.coerceIn(-headerHeight, neutralPosition)
            val consumed = headerOffset - previousOffset
            return Offset(0f, consumed.toFloat())
        }

        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y.toInt()
            val newOffset = headerOffset + delta
            val previousOffset = headerOffset
            if (newOffset < previousOffset) {
                return Offset(x=0f, y=0f)
            }
            headerOffset = newOffset.coerceIn(-headerHeight, neutralPosition)
            val consumed = headerOffset - previousOffset
            return Offset(0f, consumed.toFloat())
        }
    }

    val headerConnection = remember() {
        CollapsingHeaderNestedScrollConnection()
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(0xFFFDF8F8))
    ){


        when {
            uiState.viewingFollowers -> {
                FollowList(viewModel,"FOLLOWERS",
                    onClickUser = { u ->
                        viewModel.setViewingFollowers(false);
                    navController.navigate(Routes.Profile.route + "/" + u);
                    }
                )
            }

            uiState.viewingFollowing -> {
                FollowList(viewModel,"FOLLOWING",
                    onClickUser = { u ->
                        viewModel.setViewingFollowing(false);
                        navController.navigate(Routes.Profile.route + "/" + u)
                    }
                )
            }

            true -> {
                Card (
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .onPlaced {
                            headerConnection.headerHeight = it.size.height
                        }
                        .offset { IntOffset(0, headerConnection.headerOffset) }
                        .border(border=BorderStroke(width = 1.dp, color = Color(0xFF6650a4)))
                        .padding(10.dp),
                ) {
                    ProfileHeader(
                        viewModel = viewModel,
                        onEditProfile = {
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("editUsername", username)

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("editBio", uiState.bio)

                            navController.navigate(Routes.EditUser.route)
                        }
                    )
                }

                        Column(
                            modifier = Modifier
                                .padding(top=10.dp,start=20.dp,end=20.dp)
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .offset { IntOffset(0, headerConnection.headerOffset + headerConnection.headerHeight) }
                                .nestedScroll(headerConnection),
                            verticalArrangement = Arrangement.spacedBy(10.dp),

                            ) {

                                when {
                                    uiState.username.isEmpty() -> {}

                                    true -> {
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                                .padding(vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                        ) {
                                            Row (
                                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                            ) {

                                                when {
                                                    !uiState.viewingReviews -> {
                                                        OutlinedButton(
                                                            shape = RoundedCornerShape(35),
                                                            onClick = { viewModel.fetchPosts(username) }
                                                        ) {
                                                            Text("Posts")
                                                        }

                                                        ElevatedButton(
                                                            shape = RoundedCornerShape(35),
                                                            onClick = { viewModel.fetchReviews(username) }
                                                        ) {
                                                            Text("Reviews")
                                                        }
                                                    }
                                                    uiState.viewingReviews -> {
                                                        ElevatedButton(
                                                            shape = RoundedCornerShape(35),
                                                            onClick = { viewModel.fetchPosts(username) }
                                                        ) {
                                                            Text("Posts")
                                                        }

                                                        OutlinedButton(
                                                            shape = RoundedCornerShape(35),
                                                            onClick = { viewModel.fetchReviews(username) }
                                                        ) {
                                                            Text("Reviews")
                                                        }
                                                    }
                                                }
                                            }

                                            if (uiState.username == MainActivity.loggedInUser) {
                                                IconButton(
                                                    onClick = {
                                                        navController.currentBackStackEntry?.savedStateHandle?.set("postId", null)
                                                        navController.navigate(Routes.PostEditor.route)
                                                    },
                                                    content = {
                                                        Icon(
                                                            modifier = Modifier.size(60.dp),
                                                            imageVector = Icons.Filled.Add,
                                                            contentDescription = "Add Post",
                                                            tint = Color(0xFF6650a4),
                                                        )
                                                    }
                                                )
                                            }

                                        }

                                        when {
                                            uiState.loadingBody -> {
                                                CircularProgressIndicator()
                                            }

                                            uiState.username.isEmpty() -> {

                                            }

                                            !uiState.bodyError.isEmpty() -> {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Text(
                                                        text = uiState.bodyError,
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }

                                            !uiState.viewingReviews -> {
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight()
                                                ) {
                                                    ProfilePosts(viewModel,navController,username)
                                                }
                                            }

                                            uiState.viewingReviews -> {
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight()
                                                ) {
                                                    ProfileReviews(viewModel,navController,username)
                                                }
                                            }
                                        }
                                    }
                                }


                        }
            }
        }
    }
}






//@Preview
//@Composable
//fun ProfilePagePreview(
//    username: String = "Sample User",
//    //navController: NavController,
//    //viewModel: ProfileViewModel,
//    posts: List<PostResponse> = List(10,
//        {
//                PostResponse(it,text="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                "10-10-25")
//            }),
//    reviews: List<ReviewResponse> = List(10,
//        {
//            ReviewResponse(it,text="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                "10-10-25",3.5,it)
//        }),
//    viewingReviews:Boolean = false,
//    viewingFollowers: Boolean = false,
//    viewingFollowing: Boolean = false,
//    ownProfile: Boolean = true,
//){
//    // Observe state
//    //val uiState by viewModel.uiState.collectAsState()
//
//   // LaunchedEffect(key1 = MainActivity.bearerToken) {
//    //    viewModel.fetchProfile(username)
//        //viewModel.fetchPosts(username)
//        //viewModel.fetchReviews(username,false)
//   // }
//
//    class CollapsingHeaderNestedScrollConnection(
//    ) : NestedScrollConnection {
//
//        var neutralPosition: Int by mutableIntStateOf(0)
//
//        var headerHeight: Int by mutableIntStateOf(0)
//
//        var headerOffset: Int by mutableIntStateOf(0)
//            private set
//
//        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//            val delta = available.y.toInt()
//            val newOffset = headerOffset + delta
//            val previousOffset = headerOffset
//            if (newOffset > previousOffset) {
//                return Offset(x=0f, y=0f)
//            }
//            headerOffset = newOffset.coerceIn(-headerHeight, neutralPosition)
//            val consumed = headerOffset - previousOffset
//            return Offset(0f, consumed.toFloat())
//        }
//
//        override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
//            val delta = available.y.toInt()
//            val newOffset = headerOffset + delta
//            val previousOffset = headerOffset
//            if (newOffset < previousOffset) {
//                return Offset(x=0f, y=0f)
//            }
//            headerOffset = newOffset.coerceIn(-headerHeight, neutralPosition)
//            val consumed = headerOffset - previousOffset
//            return Offset(0f, consumed.toFloat())
//        }
//    }
//
//    val headerConnection = remember() {
//        CollapsingHeaderNestedScrollConnection()
//    }
//
//    Box(
//           modifier = Modifier.fillMaxSize()
//               .background(color = Color(0xFFFDF8F8))
//    ){
//
//        when {
//            viewingFollowers -> {
//                FollowListPreview("FOLLOWERS")
//            }
//
//            viewingFollowing -> {
//                FollowListPreview( "FOLLOWING")
//            }
//
//            true -> {
//                Card (
//                    modifier = Modifier
//                        .height(IntrinsicSize.Min)
//                        .fillMaxWidth()
//                        .onPlaced {
//                            headerConnection.headerHeight = it.size.height
//                        }
//                        .offset { IntOffset(0, headerConnection.headerOffset) }
//                        .border(border=BorderStroke(width = 1.dp, color = Color(0xFF6650a4)))
//                        .padding(10.dp),
//                ) {
//                    ProfileHeaderPreview()
//                }
//
//
//                Column(
//                    modifier = Modifier
//                        .padding(top=10.dp,start=20.dp,end=20.dp)
//                        .fillMaxWidth()
//                        .fillMaxHeight()
//                        .offset { IntOffset(0, headerConnection.headerOffset + headerConnection.headerHeight) }
//                        .nestedScroll(headerConnection),
//                    verticalArrangement = Arrangement.spacedBy(10.dp),
//
//                    ) {
//                    when {
//                        !viewingReviews -> {
//
//                            Row(
//                                modifier = Modifier.fillMaxWidth()
//                                    .padding(vertical = 10.dp),
//                                horizontalArrangement = Arrangement.SpaceBetween,
//                            ) {
//                                Row (
//                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
//                                ) {
//                                    OutlinedButton(
//                                        shape = RoundedCornerShape(35),
//                                        onClick = { }
//                                    ) {
//                                        Text("Posts")
//                                    }
//
//                                    ElevatedButton(
//                                        shape = RoundedCornerShape(35),
//                                        onClick = { }
//                                    ) {
//                                        Text("Reviews")
//                                    }
//                                }
//
//                                if (ownProfile) {
//                                    IconButton(
//                                        //modifier = Modifier.fillMaxWidth(),
//                                        //shape = RoundedCornerShape(35),
//                                        onClick = {
//                                            //  if (type == followListType.FOLLOWERS) {
//                                            //       viewModel.setViewingFollowers(false)
//                                            //   } else {
//                                            //       viewModel.setViewingFollowing(false)
//                                            //   }
//                                        },
//                                        content = {
//                                            Icon(
//                                                modifier = Modifier.size(60.dp),
//                                                imageVector = Icons.Filled.Add,
//                                                contentDescription = "Add Post",
//                                                tint = Color(0xFF6650a4),
//                                            )
//                                        }
//                                    )
//                                }
//
//                            }
//                            Card(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .fillMaxHeight()
//                            ) {
//                                ProfilePostsPreview(username)
//                            }
//                        }
//
//                        viewingReviews -> {
//                            Row(
//                                modifier = Modifier.fillMaxWidth()
//                                    .padding(vertical = 10.dp),
//                                horizontalArrangement = Arrangement.SpaceBetween,
//                            ) {
//                                Row(
//                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
//                                ) {
//                                    ElevatedButton(
//                                        shape = RoundedCornerShape(35),
//                                        onClick = { }
//                                    ) {
//                                        Text("Posts")
//                                    }
//
//                                    OutlinedButton(
//                                        shape = RoundedCornerShape(35),
//                                        onClick = { }
//                                    ) {
//                                        Text("Reviews")
//                                    }
//                                }
//
//                                if (ownProfile) {
//                                    IconButton(
//                                        //modifier = Modifier.fillMaxWidth(),
//                                        //shape = RoundedCornerShape(35),
//                                        onClick = {
//                                            //  if (type == followListType.FOLLOWERS) {
//                                            //       viewModel.setViewingFollowers(false)
//                                            //   } else {
//                                            //       viewModel.setViewingFollowing(false)
//                                            //   }
//                                        },
//                                        content = {
//                                            Icon(
//                                                modifier = Modifier.size(60.dp),
//                                                imageVector = Icons.Filled.Add,
//                                                contentDescription = "Add Review",
//                                                tint = Color(0xFF6650a4),
//                                            )
//                                        }
//                                    )
//                                }
//
//                            }
//                            Card(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .fillMaxHeight()
//                            ) {
//                                ProfileReviewsPreview(username)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }
//}