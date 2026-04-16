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
import androidx.compose.foundation.layout.size
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
            .wrapContentHeight()
    ) {
        when {
            uiState.loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error.isNotEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            uiState.profileData == null -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
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
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                    ) {
                        val profileBitmap = base64ToImageBitmap(uiState.profileData?.profilePictureBase64)

                        if (profileBitmap != null) {
                            Image(
                                painter = BitmapPainter(profileBitmap),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile_pic_placeholder),
                                contentDescription = "Profile Picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                            )
                        }

                        Text(
                            modifier = Modifier.weight(1f),
                            text = uiState.profileData?.username ?: "",
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (uiState.profileData!!.username != MainActivity.loggedInUser) {
                            Button(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(35),
                                onClick = {
                                    if (!uiState.amFollowing) {
                                        viewModel.follow(uiState.profileData!!.username)
                                    } else {
                                        viewModel.unfollow(uiState.profileData!!.username)
                                    }
                                }
                            ) {
                                Text(
                                    text = if (!uiState.amFollowing) "Follow" else "Unfollow",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 13.sp
                                )
                            }
                        } else {
                            Button(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(35),
                                onClick = onEditProfile
                            ) {
                                Text(
                                    text = "Edit Profile",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.weight(2f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { viewModel.setViewingFollowers(true) }
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = uiState.profileData!!.followers.size.toString(),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Followers",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                            TextButton(
                                onClick = { viewModel.setViewingFollowing(true) }
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = uiState.profileData!!.following.size.toString(),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Following",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }

                    if (!uiState.profileData!!.bio.isNullOrBlank()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            text = uiState.profileData!!.bio!!,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileHeaderPreview() {
    val sampleFollowers = listOf(FollowResponse("u1"), FollowResponse("u2"))
    
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_pic_placeholder),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(80.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = "Sample User",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(35),
                    onClick = { }
                ) {
                    Text("Edit Profile")
                }

                Row(
                    modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("123", style = MaterialTheme.typography.titleMedium)
                            Text("Followers", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    TextButton(onClick = { }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("456", style = MaterialTheme.typography.titleMedium)
                            Text("Following", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            Text(
                text = "This is a sample bio that should now have much better padding and alignment.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }
    }
}