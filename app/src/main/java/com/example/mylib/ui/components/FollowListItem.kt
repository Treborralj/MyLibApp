package com.example.mylib.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mylib.R
import com.example.mylib.data.models.BookResponse
import com.example.mylib.data.models.FollowResponse


@Composable
fun FollowListItem(
    account: FollowResponse,
    //onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        // .clickable{onClick()}
    ){
        Row(
            modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ){

            Box(
                modifier = Modifier.fillMaxWidth(0.3f).fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_pic_placeholder),
                    contentDescription = "Profile Picture",
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                )
            }


            Box(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = account.username,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                )
            }




        }
    }
}






@Preview
@Composable
fun FollowListItemPreview(
    //account: FollowResponse = FollowResponse(username = "Sample User"),
    //onClick: () -> Unit
){
    val account = FollowResponse(username = "Sample User");
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
           // .clickable{onClick()}
    ){
        Row(
            modifier = Modifier.padding(vertical = 5.dp).
            fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp,Alignment.Start)
        ){

            Box(
                modifier = Modifier.fillMaxWidth(0.3f).fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_pic_placeholder),
                    contentDescription = "Profile Picture",
                    alignment = Alignment.Center,
                    //modifier = Modifier.size(width = 100.dp, height = 120.dp)
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                )
            }


            Box(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = account.username,
                    style = MaterialTheme.typography.titleLarge,
                    //maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    //textAlign = TextAlign.Center
                )
            }




        }
    }
}