package com.example.mylib.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mylib.data.models.FollowResponse
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylib.MainActivity
import com.example.mylib.R
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.ProfileViewModel


@Composable
fun ProfileHeader(
    viewModel: ProfileViewModel,
) {

    // Observe state
    val uiState by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            when {
                uiState.loading -> {
                    CircularProgressIndicator()
                }

                !uiState.error.isEmpty() -> {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                uiState.profileData == null -> {
                    Text(
                        text = "User data not found",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                uiState.profileData != null -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth().padding(),
                        horizontalArrangement = Arrangement.Start
                    ) {

                        Image(
                            painter = painterResource(R.drawable.profile_pic_placeholder),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(width = 100.dp, height = 120.dp)
                        )

                        Column(
                            modifier = Modifier.padding(5.dp),
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    modifier = Modifier.padding(top = 10.dp),
                                    text = uiState.profileData!!.username,
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    //textAlign = TextAlign.Center
                                )

                                if (uiState.profileData!!.username != MainActivity.loggedInUser) {

                                    when {
                                        !uiState.amFollowing -> {
                                            Button(
                                                shape = RoundedCornerShape(35),
                                                onClick = {
                                                    viewModel.follow(uiState.profileData!!.username)
                                                }) {
                                                Text(
                                                    text = "Follow",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                        uiState.amFollowing -> {
                                            Button(
                                                shape = RoundedCornerShape(35),
                                                onClick = {
                                                    viewModel.unfollow(uiState.profileData!!.username)
                                                }) {
                                                Text(
                                                    text = "Unfollow",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontSize = 13.sp
                                                )
                                            }
                                        }
                                    }

                                } else {
                                    Button(
                                        shape = RoundedCornerShape(35),
                                        onClick = {
                                            //todo
                                        }) {
                                        Text(
                                            text = "Edit Profile",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                            }

                            Row() {

                                TextButton(
                                    shape = RoundedCornerShape(25),
                                    onClick = {

                                    }) {
                                    Text(
                                        text = uiState.profileData!!.followers.size.toString() + "\nFollowers",
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }

                                TextButton(
                                    shape = RoundedCornerShape(25),
                                    onClick = {

                                    }) {
                                    Text(
                                        text = uiState.profileData!!.following.size.toString() + "\nFollowing",
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = uiState.profileData!!.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}











@Preview
@Composable
fun ProfileHeaderPreview(
    username: String = "Sample User",
    bio: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    //profilePic:
    //viewModel: ProfileViewModel,
    followers: List<FollowResponse> = listOf(FollowResponse("Sample User 1"), FollowResponse("Sample User 2"), FollowResponse("Sample User 3")),
    following: List<FollowResponse> = listOf(FollowResponse("Sample User 1"), FollowResponse("Sample User 2"), FollowResponse("Sample User 3")),
) {

    // Observe state
    //val uiState by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {

                    Image(
                        painter = painterResource(R.drawable.profile_pic_placeholder),
                        contentDescription = "Profile Picture",
                        //modifier = Modifier.size(width = 100.dp, height = 120.dp)
                        modifier = Modifier.fillMaxWidth(0.3f).fillMaxHeight(0.3f),
                    )

                Box(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding().fillMaxWidth(1f),
                        text = username,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Button(
                    modifier = Modifier.fillMaxWidth(0.3f),
                    shape = RoundedCornerShape(35),
                    onClick = {

                    }) {
                    Text(
                        text = "Follow",
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        shape = RoundedCornerShape(25),
                        onClick = {

                        }) {
                        Text(
                            text = followers.size.toString() + "\nFollowers",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                        )
                    }

                    TextButton(
                        shape = RoundedCornerShape(25),
                        onClick = {

                        }) {
                        Text(
                            text = following.size.toString() + "\nFollowing",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                }


            }


            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }

        }
    }

}