package com.example.mylib.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun LoginPage(navController: NavController){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            onClick = {
                navController.navigate("homeFeedPage")
            }
        ){
            Text("Login")
        }
        Button(
            onClick = {
                navController.navigate("signupPage")
            }
        ) {
            Text("Click here to signup")
        }
    }
}