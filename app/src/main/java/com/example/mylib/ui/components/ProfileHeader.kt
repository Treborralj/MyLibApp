package com.example.mylib.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylib.MainActivity
import com.example.mylib.R
import com.example.mylib.data.models.FollowResponse
import com.example.mylib.viewModel.ProfileViewModel
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import com.example.mylib.ui.util.base64ToImageBitmap
@Composable
fun ProfileHeader(
    viewModel: ProfileViewModel,
    onEditProfile: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
    ) {
        when {
            uiState.loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error.isNotEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding()
                            .fillMaxWidth(),
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            uiState.profileData == null -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Profile not found",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        val profileBitmap = base64ToImageBitmap(uiState.profileData?.profilePictureBase64)

                        if (profileBitmap != null) {
                            Image(
                                painter = BitmapPainter(profileBitmap),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile_pic_placeholder),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),
                            )
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding()
                                    .fillMaxWidth(),
                                text = uiState.profileData?.username ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (uiState.profileData!!.username != MainActivity.loggedInUser) {
                            if (!uiState.amFollowing) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(0.3f),
                                    shape = RoundedCornerShape(35),
                                    onClick = {
                                        viewModel.follow(uiState.profileData!!.username)
                                    }
                                ) {
                                    Text(
                                        text = "Follow",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                Button(
                                    modifier = Modifier.fillMaxWidth(0.3f),
                                    shape = RoundedCornerShape(35),
                                    onClick = {
                                        viewModel.unfollow(uiState.profileData!!.username)
                                    }
                                ) {
                                    Text(
                                        text = "Unfollow",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            Button(
                                modifier = Modifier.fillMaxWidth(0.3f),
                                shape = RoundedCornerShape(35),
                                onClick = onEditProfile
                            ) {
                                Text(
                                    text = "Edit\nProfile",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                shape = RoundedCornerShape(25),
                                onClick = {
                                    viewModel.setViewingFollowers(true)
                                }
                            ) {
                                Text(
                                    text = uiState.profileData!!.followers.size.toString() + "\nFollowers",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center,
                                )
                            }

                            TextButton(
                                shape = RoundedCornerShape(25),
                                onClick = {
                                    viewModel.setViewingFollowing(true)
                                }
                            ) {
                                Text(
                                    text = uiState.profileData!!.following.size.toString() + "\nFollowing",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            text = uiState.profileData!!.bio ?: "No Bio",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
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
    followers: List<FollowResponse> = listOf(
        FollowResponse("Sample User 1"),
        FollowResponse("Sample User 2"),
        FollowResponse("Sample User 3")
    ),
    following: List<FollowResponse> = listOf(
        FollowResponse("Sample User 1"),
        FollowResponse("Sample User 2"),
        FollowResponse("Sample User 3")
    ),
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_pic_placeholder),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxWidth(0.3f),
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding()
                            .fillMaxWidth(),
                        text = username,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(0.3f),
                    shape = RoundedCornerShape(35),
                    onClick = { }
                ) {
                    Text(
                        text = "Edit\nProfile",
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
                        onClick = { }
                    ) {
                        Text(
                            text = followers.size.toString() + "\nFollowers",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                        )
                    }

                    TextButton(
                        shape = RoundedCornerShape(25),
                        onClick = { }
                    ) {
                        Text(
                            text = following.size.toString() + "\nFollowing",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    text = bio,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}