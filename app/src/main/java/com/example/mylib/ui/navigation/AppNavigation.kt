package com.example.mylib.ui.navigation

import BookRepository
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
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.remote.RetrofitClient
import com.example.mylib.data.repo.AuthenticationRepository
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.SearchRepository
import com.example.mylib.data.repo.UserRepository
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
import com.example.mylib.ui.screens.BookPage
import com.example.mylib.ui.screens.PostEditor
import com.example.mylib.ui.screens.ReviewEditor
import com.example.mylib.viewModel.BookViewModel
import com.example.mylib.viewModel.HomefeedViewModel
import com.example.mylib.viewModel.PostEditorViewModel
import com.example.mylib.viewModel.ProfileViewModel
import com.example.mylib.viewModel.ReviewEditorViewModel
import com.example.mylib.viewModel.factory.BookViewModelFactory
import com.example.mylib.viewModel.factory.HomefeedViewModelFactory
import com.example.mylib.viewModel.factory.PostEditorViewModelFactory
import com.example.mylib.viewModel.factory.ProfileViewModelFactory
import com.example.mylib.viewModel.factory.ReviewEditorViewModelFactory

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    val authenticationRepository = AuthenticationRepository(RetrofitClient.authenticationApi)
    val authenticationFactory = AuthenticationViewModelFactory(authenticationRepository)
    val authenticationViewModel: AuthenticationViewModel = viewModel(factory = authenticationFactory)

    val searchRepository = SearchRepository(RetrofitClient.bookApi, RetrofitClient.userApi)
    val bookFactory = SearchViewModelFactory(searchRepository)
    val searchViewModel: SearchViewModel = viewModel(factory = bookFactory)

    val userRepository = UserRepository(RetrofitClient.userApi)
    val homefeedFactory = HomefeedViewModelFactory(userRepository)
    val homeFeedViewModel: HomefeedViewModel = viewModel(factory = homefeedFactory)

    val navigationBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navigationBackStackEntry?.destination?.route
    val bookRepository = BookRepository(RetrofitClient.bookApi)
    val reviewRepository = ReviewRepository(RetrofitClient.reviewApi)
    val bookViewModelFactory = BookViewModelFactory(bookRepository,reviewRepository)

    val postRepository = PostRepository(RetrofitClient.postApi)
    val profileFactory = ProfileViewModelFactory(userRepository,postRepository,reviewRepository)
    val profileViewModel: ProfileViewModel = viewModel(factory = profileFactory)

    val postEditorFactory = PostEditorViewModelFactory(postRepository)
    val reviewEditorFactory = ReviewEditorViewModelFactory(reviewRepository)

    val showBottomBar = currentRoute in listOf(
        Routes.Home.route,
        Routes.Search.route,
        Routes.Profile.route,
        Routes.Lists.route,
        "bookPage/{bookId}"
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
            composable("bookPage/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments
                    ?.getString("bookId")
                    ?.toIntOrNull()
                    ?: return@composable

                val bookViewModel: BookViewModel = viewModel(factory = bookViewModelFactory)
                BookPage(
                    bookId = bookId,
                    viewModel = bookViewModel,
                    onAddReview = { }
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
                HomeFeedPage(navController,homeFeedViewModel)

            }
            composable(Routes.Search.route){
                SearchPage(
                    viewModel = searchViewModel,
                    onBookClick = { book ->
                        navController.navigate("bookPage/${book.id}")
                    },
                    onUserClick = { }
                )
            }
            composable(Routes.Profile.route + "/{username}"){ backStackEntry ->
                val username = backStackEntry.arguments
                    ?.getString("username")
                    ?: return@composable
                ProfilePage(username = username,viewModel = profileViewModel, navController = navController)
            }
            composable(Routes.Lists.route){
                ListPage()
            }

            composable(Routes.PostEditor.route) {
                val id = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("postId")
                val text = navController.previousBackStackEntry?.savedStateHandle?.get<String>("postText")
                val time = navController.previousBackStackEntry?.savedStateHandle?.get<String>("postTime")


                val post = PostResponse(
                    id = id,
                    text = text,
                    time = time,
                )

                val postEditorViewModel: PostEditorViewModel = viewModel(factory = postEditorFactory)
                PostEditor(
                    viewModel = postEditorViewModel,
                    post = post,
                    navController = navController
                )
            }
            composable(Routes.ReviewEditor.route) {

                val id = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("reviewId")
                val text = navController.previousBackStackEntry?.savedStateHandle?.get<String>("reviewText")
                val time = navController.previousBackStackEntry?.savedStateHandle?.get<String>("reviewTime")
                val score = navController.previousBackStackEntry?.savedStateHandle?.get<Double>("reviewScore")

                val review = ReviewResponse(
                    id = id,
                    text = text,
                    time = time,
                    score = score,
                )

                val reviewEditorViewModel: ReviewEditorViewModel = viewModel(factory = reviewEditorFactory)
                ReviewEditor(
                    viewModel = reviewEditorViewModel,
                    review = review,
                    bookTitle = "Book Title",
                    bookId = null,
                    navController = navController
                )
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

    data object PostEditor: Routes("postEditorPage")
    data object ReviewEditor: Routes("reviewEditorPage")
}