package com.example.mylib.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mylib.data.models.FollowResponse
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylib.MainActivity
import com.example.mylib.ui.navigation.Routes
import com.example.mylib.viewModel.ProfileViewModel
import com.example.mylib.R

@Composable
fun ProfileHeader(
    viewModel: ProfileViewModel,
) {

    // Observe state
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
    ) {

        when {
            uiState.loading || uiState.profileData == null -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            !uiState.error.isEmpty()  -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding().fillMaxWidth(1f),
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyLarge,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            true -> {
                Column(
                    modifier = Modifier.padding(start=15.dp, end=15.dp)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth().padding()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                    ) {

                        Image(
                            painter = painterResource(R.drawable.profile_pic_placeholder),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxWidth(0.3f),
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding().fillMaxWidth(1f),
                                text = uiState.profileData?.username ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        if (uiState.profileData!!.username != MainActivity.loggedInUser) {
                            when {
                                !uiState.amFollowing -> {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(0.3f),
                                        shape = RoundedCornerShape(35),
                                        onClick = {
                                            viewModel.follow(uiState.profileData!!.username)
                                        }) {
                                        Text(
                                            text = "Follow",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                uiState.amFollowing -> {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(0.3f),
                                        shape = RoundedCornerShape(35),
                                        onClick = {
                                            viewModel.unfollow(uiState.profileData!!.username)
                                        }) {
                                        Text(
                                            text = "Unfollow",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            Button(
                                modifier = Modifier.fillMaxWidth(0.3f),
                                shape = RoundedCornerShape(35),
                                onClick = {
                                    //todo
                                }) {
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
                                    viewModel.setViewingFollowers(true);
                                }) {
                                Text(
                                    text = uiState.profileData!!.followers.size.toString() + "\nFollowers",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center,
                                )
                            }

                            TextButton(
                                shape = RoundedCornerShape(25),
                                onClick = {
                                    viewModel.setViewingFollowing(true);
                                }) {
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
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                            ,
                            text = uiState.profileData!!.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            //overflow = TextOverflow.Ellipsis,
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
    shortbio: String = "Lorem ipsum",
    //profilePic:
    //viewModel: ProfileViewModel,
    followers: List<FollowResponse> = listOf(FollowResponse("Sample User 1"), FollowResponse("Sample User 2"), FollowResponse("Sample User 3")),
    following: List<FollowResponse> = listOf(FollowResponse("Sample User 1"), FollowResponse("Sample User 2"), FollowResponse("Sample User 3")),
) {

    // Observe state
    //val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
           // .wrapContentHeight()
            //.fillMaxHeight()
                .height(intrinsicSize = IntrinsicSize.Min)
          //  .border(border=BorderStroke(width = 3.dp, color = Color(0xFF22EC1C)))
           // .background(Color(0xFF22EC1C))
    ) {
        Column(
            modifier = Modifier.padding(start=15.dp, end=15.dp)
               // .height(intrinsicSize = IntrinsicSize.Min)
                .wrapContentHeight()
           //     .background(Color(0xFF1CDBEC))
            ,
            //verticalArrangement = Arrangement.SpaceBetween
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier
            //        .border(border=BorderStroke(width = 3.dp, color = Color(0xFFCD1CEC)))
                    .fillMaxWidth().padding()
                       .wrapContentHeight()
                //    .height(IntrinsicSize.Min)
                    ,
                verticalAlignment = Alignment.CenterVertically
                ,
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {

                    Image(
                        painter = painterResource(R.drawable.profile_pic_placeholder),
                        contentDescription = "Profile Picture",
                        //modifier = Modifier.size(width = 100.dp, height = 120.dp)
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                          //  .fillMaxHeight(0.4f)
             //               .border(border=BorderStroke(width = 3.dp, color = Color(0xFFEEF60A)))
                        ,
                    )

                Box(
                    modifier = Modifier.fillMaxWidth()
                     //   .fillMaxHeight(0.3f)
                    ,
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
                    .fillMaxWidth().padding()
                //    .border(border=BorderStroke(width = 3.dp, color = Color(0xFFCD1CEC)))
                ,
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Button(
                    modifier = Modifier.fillMaxWidth(0.3f),
                    shape = RoundedCornerShape(35),
                    onClick = {

                    }) {
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
                modifier = Modifier
                 //   .wrapContentHeight()
                    .fillMaxWidth()
                    .weight(1f)
                 //   .border(border=BorderStroke(width = 3.dp, color = Color(0xFFCD1CEC)))
                ,
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                    ,
                    text = shortbio,
                    style = MaterialTheme.typography.bodyMedium,
                    //overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }

        }
    }

}