package com.example.mylib.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mylib.data.remote.RetrofitClient
import com.example.mylib.data.repo.AuthenticationRepository
import com.example.mylib.data.repo.SearchRepository
import com.example.mylib.ui.screens.SearchPage
import com.example.mylib.ui.screens.HomeFeedPage
import com.example.mylib.ui.screens.ListPage
import com.example.mylib.ui.screens.LoginPage
import com.example.mylib.ui.screens.ProfilePage
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

    val navigationBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navigationBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Routes.Home.route,
        Routes.Search.route,
        Routes.Profile.route,
        Routes.Lists.route
    )

    Scaffold(
        bottomBar = {
            if(showBottomBar){
                BottomNavBar(navController = navController)
            }
        }
    ){
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Login.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Routes.Login.route){
                LoginPage(
                    viewModel = authenticationViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.Home.route){
                            popUpTo(Routes.Login.route){inclusive = true}
                        }
                    },
                    onGoToSignup = {
                        navController.navigate(Routes.Signup.route)
                    }
                )
            }
            composable(Routes.Signup.route){
                SignupPage(
                    viewModel = authenticationViewModel,
                    onSignupFinished = {
                        navController.navigate(Routes.Home.route){
                            popUpTo(Routes.Signup.route){inclusive = true}
                        }
                    }
                )
            }
            composable(Routes.Home.route){
                HomeFeedPage(navController)

            }
            composable(Routes.Search.route){
                SearchPage(
                    viewModel = searchViewModel,
                    onBookClick = { },
                    onUserClick = { }
                )
            }
            composable(Routes.Profile.route){
                ProfilePage()
            }
            composable(Routes.Lists.route){
                ListPage()
            }
        }
    }
}

sealed class Routes(val route: String){
    data object Login : Routes("loginPage")
    data object Signup: Routes("signupPage")

    data object Home: Routes("homeFeedPage")
    data object Search: Routes("searchPage")
    data object Profile: Routes("profilePage")
    data object Lists: Routes("listPage")
}