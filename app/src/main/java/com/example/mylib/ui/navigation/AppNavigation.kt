package com.example.mylib.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylib.data.remote.RetrofitClient
import com.example.mylib.data.repo.AuthenticationRepository
import com.example.mylib.data.repo.BookRepository
import com.example.mylib.ui.screens.BookSearchPage
import com.example.mylib.ui.screens.HomeFeedPage
import com.example.mylib.ui.screens.LoginPage
import com.example.mylib.ui.screens.SignupPage
import com.example.mylib.viewModel.AuthenticationViewModel
import com.example.mylib.viewModel.BookSearchViewModel
import com.example.mylib.viewModel.factory.AuthenticationViewModelFactory
import com.example.mylib.viewModel.factory.BookSearchViewModelFactory

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    val authenticationRepository = AuthenticationRepository(RetrofitClient.authenticationApi)
    val authenticationFactory = AuthenticationViewModelFactory(authenticationRepository)
    val authenticationViewModel: AuthenticationViewModel = viewModel(factory = authenticationFactory)

    val bookRepository = BookRepository(RetrofitClient.bookApi)
    val bookFactory = BookSearchViewModelFactory(bookRepository)
    val bookSearchViewModel: BookSearchViewModel = viewModel(factory = bookFactory)

    NavHost(
        navController = navController,
        startDestination = "loginPage"
    ){
        composable("loginPage"){
            LoginPage(
                viewModel = authenticationViewModel,
                onLoginSuccess = {
                    navController.navigate("bookSearchPage"){
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
        composable("bookSearchPage"){
            BookSearchPage(
                viewModel = bookSearchViewModel,
                onBookClick = { }
            )
        }
    }
}