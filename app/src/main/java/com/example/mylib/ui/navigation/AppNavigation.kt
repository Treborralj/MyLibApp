package com.example.mylib.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylib.data.remote.RetrofitClient
import com.example.mylib.data.repo.AuthenticationRepository
import com.example.mylib.ui.screens.HomeFeedPage
import com.example.mylib.ui.screens.LoginPage
import com.example.mylib.ui.screens.SignupPage
import com.example.mylib.viewModel.AuthenticationViewModel
import com.example.mylib.viewModel.AuthenticationViewModelFactory

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    val api = RetrofitClient.authenticationApi
    val repository = AuthenticationRepository(api)
    val factory = AuthenticationViewModelFactory(repository)
    val viewModel: AuthenticationViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = "loginPage"
    ){
        composable("loginPage"){
            LoginPage(navController, viewModel)
        }
        composable("signupPage"){
            SignupPage(navController, viewModel)

        }
        composable("homeFeedPage"){
            HomeFeedPage(navController)

        }
    }
}