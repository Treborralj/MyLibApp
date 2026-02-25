package com.example.mylib.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylib.data.remote.RetrofitClient
import com.example.mylib.data.repo.AuthenticationRepository
import com.example.mylib.data.repo.SearchRepository
import com.example.mylib.ui.screens.SearchPage
import com.example.mylib.ui.screens.HomeFeedPage
import com.example.mylib.ui.screens.LoginPage
import com.example.mylib.ui.screens.SignupPage
import com.example.mylib.viewModel.authentication.AuthenticationViewModel
import com.example.mylib.viewModel.search.SearchViewModel
import com.example.mylib.viewModel.factory.AuthenticationViewModelFactory
import com.example.mylib.viewModel.factory.SearchViewModelFactory

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    val authenticationRepository = AuthenticationRepository(RetrofitClient.authenticationApi)
    val authenticationFactory = AuthenticationViewModelFactory(authenticationRepository)
    val authenticationViewModel: AuthenticationViewModel = viewModel(factory = authenticationFactory)

    val searchRepository = SearchRepository(RetrofitClient.bookApi, RetrofitClient.userApi)
    val bookFactory = SearchViewModelFactory(searchRepository)
    val searchViewModel: SearchViewModel = viewModel(factory = bookFactory)

    NavHost(
        navController = navController,
        startDestination = "loginPage"
    ){
        composable("loginPage"){
            LoginPage(
                viewModel = authenticationViewModel,
                onLoginSuccess = {
                    navController.navigate("searchPage"){
                        popUpTo("loginPage"){inclusive = true}
                    }
                },
                onGoToSignup = {
                    navController.navigate("signupPage")
                }
            )
        }
        composable("signupPage"){
            SignupPage(
                viewModel = authenticationViewModel,
                onSignupFinished = {
                    navController.navigate("loginPage"){
                        popUpTo("singupPage"){inclusive = true}
                    }
                }
            )
        }
        composable("homeFeedPage"){
            HomeFeedPage(navController)

        }
        composable("searchPage"){
            SearchPage(
                viewModel = searchViewModel,
                onBookClick = { },
                onUserClick = { }
            )
        }
    }
}