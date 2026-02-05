package com.example.mylib.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylib.ui.screens.HomeFeedPage
import com.example.mylib.ui.screens.LoginPage
import com.example.mylib.ui.screens.SignupPage

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "loginPage"
    ){
        composable("loginPage"){
            LoginPage(navController)
        }
        composable("signupPage"){
            SignupPage(navController)

        }
        composable("homeFeedPage"){
            HomeFeedPage(navController)

        }
    }
}